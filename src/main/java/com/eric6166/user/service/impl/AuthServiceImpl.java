package com.eric6166.user.service.impl;

import com.eric6166.base.dto.MessageResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppValidationException;
import com.eric6166.base.exception.ErrorCode;
import com.eric6166.base.exception.ErrorDetail;
import com.eric6166.base.exception.ValidationErrorDetail;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import com.eric6166.keycloak.config.KeycloakAminClient;
import com.eric6166.keycloak.validation.UserValidator;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.security.utils.SecurityConst;
import com.eric6166.user.dto.GetTokenRequest;
import com.eric6166.user.dto.RegisterAccountRequest;
import com.eric6166.user.service.AuthService;
import jakarta.ws.rs.WebApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakAminClient keycloakAminClient;
    private final UserValidator userValidator;
    private final MessageSource messageSource;
    private final AppSecurityUtils appSecurityUtils;

    @Override
    public Object test() {
        Map<String, Object> response = new HashMap<>();
        response.put("username", AppSecurityUtils.getUsername());
        response.put("email", AppSecurityUtils.getEmail());
        response.put("sessionId", AppSecurityUtils.getSessionId());
        response.put("authorizationHeader", appSecurityUtils.getAuthorizationHeader());
        response.put("scope", AppSecurityUtils.getScope());
        response.put("authorities", AppSecurityUtils.getAuthorities());
        response.put("jwtId", AppSecurityUtils.getJwtId());
        response.put("subject", AppSecurityUtils.getSubject());
        response.put("audience", AppSecurityUtils.getAudience());
        response.put("issuedAt", AppSecurityUtils.getIssuedAt());
        response.put("expiresAt", AppSecurityUtils.getExpiresAt());
        response.put("fullName", AppSecurityUtils.getFullName());
        response.put("firstName", AppSecurityUtils.getFirstName());
        response.put("lastName", AppSecurityUtils.getLastName());
        response.put("preferredUsername", AppSecurityUtils.getPreferredUsername());
        response.put("emailVerified", AppSecurityUtils.getEmailVerified());
        response.put("issuer", AppSecurityUtils.getIssuer());
        response.put("remoteAddress", AppSecurityUtils.getRemoteAddress());
        response.put("claims", AppSecurityUtils.getClaims());
        return response;
    }

    @Override
    public Object getToken(GetTokenRequest request) throws IOException {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "grant_type=password&scope=openid offline_access&username=admin&password=P@ssw0rd");
//        String username = "microservices-auth-client";
//        String password = "123456789";
//        String credentials = username + ":" + password;
//        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());
//        Request req = new Request.Builder()
//                .url("http://localhost:8090/realms/spring-boot-microservices-realm/protocol/openid-connect/token")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Authorization", "Basic " + base64Credentials)
//                .build();
//        Response response = client.newCall(req).execute();
        return null;
    }

    @Transactional
    @Override
    public MessageResponse register(RegisterAccountRequest request) throws AppException {
        log.info("AuthServiceImpl.register"); // comment // for local testing
        List<ErrorDetail> errorDetails = new ArrayList<>();
        if (userValidator.isUsernameExisted(request.getUsername())) {
            var res = messageSource.getMessage(BaseMessageConst.MGS_RES_USERNAME, null, LocaleContextHolder.getLocale());
            var msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(BaseConst.FIELD_USERNAME, StringUtils.capitalize(msg)));
        }
        if (userValidator.isEmailExisted(request.getEmail())) {
            var res = messageSource.getMessage(BaseMessageConst.MGS_RES_EMAIL, null, LocaleContextHolder.getLocale());
            var msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(BaseConst.FIELD_EMAIL, StringUtils.capitalize(msg)));
        }
        if (CollectionUtils.isNotEmpty(errorDetails)) {
            throw new AppValidationException(ErrorCode.VALIDATION_ERROR.getReasonPhrase(), errorDetails);
        }
        var user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        var credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword()); // improvement: encryption/decryption

        user.setCredentials(Collections.singletonList(credential));
        var customerOpt = keycloakAminClient.searchGroupByName(SecurityConst.GROUP_CUSTOMER);
        if (customerOpt.isPresent()) {
            var customer = customerOpt.get();
            user.setGroups(Collections.singletonList(customer.getPath()));
        }
        user.setEnabled(true); //improvement later
        var response = keycloakAminClient.createUser(user);

        try {
            CreatedResponseUtil.getCreatedId(response);
        } catch (WebApplicationException e) {
            throw new ResponseStatusException(e.getResponse().getStatus(), e.getMessage(), e);
        }
        var res = messageSource.getMessage(BaseMessageConst.MGS_RES_ACCOUNT, null, LocaleContextHolder.getLocale());
        var msg = messageSource.getMessage(BaseMessageConst.MSG_INF_RESOURCE_CREATED, new String[]{res}, LocaleContextHolder.getLocale());
        return MessageResponse.builder()
                .message(StringUtils.capitalize(msg))
                .build();
    }
}