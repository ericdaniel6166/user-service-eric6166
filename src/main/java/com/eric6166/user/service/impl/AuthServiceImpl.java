package com.eric6166.user.service.impl;

import brave.Span;
import brave.Tracer;
import com.eric6166.base.dto.MessageResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppExceptionUtils;
import com.eric6166.base.utils.BaseMessageConstant;
import com.eric6166.keycloak.service.KeycloakAminClientService;
import com.eric6166.keycloak.validation.UserValidation;
import com.eric6166.security.utils.SecurityConst;
import com.eric6166.user.dto.GetTokenRequest;
import com.eric6166.user.dto.RegisterAccountRequest;
import com.eric6166.user.service.AuthService;
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

    KeycloakAminClientService keycloakAminClientService;
    UserValidation userValidation;
    MessageSource messageSource;
    Tracer tracer;

    @Override
    public Object getToken(GetTokenRequest request) {
        return null;
    }

    @Transactional
    @Override
    public MessageResponse register(RegisterAccountRequest request) throws AppException {
        log.debug("AuthServiceImpl.register"); // comment // for local testing
        Span span = tracer.nextSpan().name("register").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.tag("request", request.toString());
            span.annotate("userValidation.validateAccountExisted Start");
            userValidation.validateAccountExisted(request);
            span.annotate("userValidation.validateAccountExisted End");

            var user = new UserRepresentation();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());

            var credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());

            user.setCredentials(Collections.singletonList(credential));
            span.annotate("keycloakService.searchGroupByName Start");
            var customerOpt = keycloakAminClientService.searchGroupByName(SecurityConst.GROUP_CUSTOMER);
            span.annotate("keycloakService.searchGroupByName End");
            if (customerOpt.isPresent()) {
                GroupRepresentation customer = customerOpt.get();
                user.setGroups(Collections.singletonList(customer.getPath()));
            }
            user.setEnabled(true); //improvement later
            span.tag("keycloakService.createUser user username", user.getUsername());
            span.tag("keycloakService.createUser user email", user.getEmail());
            span.annotate("keycloakService.createUser Start");
            var response = keycloakAminClientService.createUser(user);
            span.annotate("keycloakService.createUser End");
            span.tag("keycloakService.createUser response status code", String.valueOf(response.getStatusInfo().getStatusCode()));
            span.tag("keycloakService.createUser response reason phrase", response.getStatusInfo().getReasonPhrase());

            try {
                var createdId = CreatedResponseUtil.getCreatedId(response);
                span.tag("keycloakService.createUser response createdId", createdId);
            } catch (WebApplicationException e) {
                span.error(e);
                throw new ResponseStatusException(e.getResponse().getStatus(), e.getMessage(), e);
            }
            var res = messageSource.getMessage(BaseMessageConstant.MGS_RES_ACCOUNT, null, LocaleContextHolder.getLocale());
            var msg = messageSource.getMessage(BaseMessageConstant.MSG_INF_RESOURCE_CREATED, new String[]{res}, LocaleContextHolder.getLocale());
            span.tag("msg", msg);
            return new MessageResponse(StringUtils.capitalize(msg));
        } catch (AppException e) {
            log.debug("e: {} , rootCause: {}", e.getClass().getName(), AppExceptionUtils.getAppExceptionRootCause(e).toString()); // comment // for local testing
            span.tag("exception.class", e.getClass().getName());
            span.tag("exception.rootCause", AppExceptionUtils.getAppExceptionRootCause(e).toString());
            span.error(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.tag("exception.class", e.getClass().getName());
            span.error(e);
            throw new AppException(e);
        } finally {
            span.finish();
        }
    }
}