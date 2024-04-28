package com.eric6166.user.service.impl;

import com.eric6166.common.dto.MessageResponse;
import com.eric6166.common.exception.AppValidationException;
import com.eric6166.common.utils.MessageConstant;
import com.eric6166.keycloak.service.KeycloakService;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.security.utils.SecurityConst;
import com.eric6166.user.config.feign.InventoryClient;
import com.eric6166.user.dto.RegisterAccountRequest;
import com.eric6166.user.service.AuthService;
import com.eric6166.user.validation.UserValidation;
import jakarta.ws.rs.WebApplicationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    KeycloakService keycloakService;
    UserValidation userValidation;
    MessageSource messageSource;
    InventoryClient inventoryClient;
    AppSecurityUtils appSecurityUtils;

    @Override
    public String testFeign(String service) {
        log.info("test feign");
        String response;
        switch (service) {
            case "inventory" -> response = inventoryClient.productTest(appSecurityUtils.getAuthorizationHeader());
            default -> response = StringUtils.EMPTY;
        }

        return response;
    }

    @Transactional
    @Override
    public MessageResponse register(RegisterAccountRequest request) throws AppValidationException {
        log.info("AuthServiceImpl.register"); // comment // for local testing
        userValidation.validateAccountExisted(request);

        var user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        var credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());

        user.setCredentials(Collections.singletonList(credential));
        var customerOpt = keycloakService.searchGroupByName(SecurityConst.GROUP_CUSTOMER);
        if (customerOpt.isPresent()) {
            GroupRepresentation customer = customerOpt.get();
            user.setGroups(Collections.singletonList(customer.getPath()));
        }
        user.setEnabled(true); //improvement later
        var response = keycloakService.createUser(user);

        try {
            CreatedResponseUtil.getCreatedId(response);
        } catch (WebApplicationException e) {
            log.info(e.getResponse().toString());
            throw new ResponseStatusException(e.getResponse().getStatus(), e.getMessage(), e);
        }
        var res = messageSource.getMessage(MessageConstant.MGS_RES_ACCOUNT, null, LocaleContextHolder.getLocale());
        var msg = messageSource.getMessage(MessageConstant.MSG_INF_RESOURCE_CREATED, new String[]{res}, LocaleContextHolder.getLocale());

        return new MessageResponse(StringUtils.capitalize(msg));
    }
}