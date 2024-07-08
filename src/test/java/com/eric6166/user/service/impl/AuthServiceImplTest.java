package com.eric6166.user.service.impl;

import com.eric6166.base.dto.MessageResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppValidationException;
import com.eric6166.base.utils.BaseMessageConst;
import com.eric6166.keycloak.config.KeycloakAminClient;
import com.eric6166.keycloak.validation.UserValidation;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.security.utils.SecurityConst;
import com.eric6166.user.dto.RegisterAccountRequest;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.GroupRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static GroupRepresentation customerGroup;
    private static RegisterAccountRequest registerAccountRequest;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private KeycloakAminClient keycloakAminClient;
    @Mock
    private UserValidation userValidation;
    @Mock
    private MessageSource messageSource;
    //        @Mock
//    private Tracer tracer;
//    @Mock
//    private Span span;
//    @Mock
//    private Tracer.SpanInScope ws;
    @Mock
    private Response response;
    @Mock
    private URI location;
    @Mock
    private AppSecurityUtils appSecurityUtils;

    @BeforeAll
    static void setUpAll() {
        customerGroup = mockGroupRepresentation("/customer");
        registerAccountRequest = mockRegisterAccountRequest("customer", "customer@customer.com");
    }

    private static GroupRepresentation mockGroupRepresentation(String path) {
        var group = new GroupRepresentation();
        group.setPath(path);
        return group;
    }

//
//    @AfterEach
//    void tearDown() {
//    }

    private static RegisterAccountRequest mockRegisterAccountRequest(String username, String email) {
        return RegisterAccountRequest.builder()
                .username(username)
                .email(email)
                .password("P@ssw0rd")
                .confirmPassword("P@ssw0rd")
                .build();
    }

    @BeforeEach
    void setUp() {
//        Mockito.when(tracer.nextSpan()).thenReturn(span);
//        Mockito.when(span.name(Mockito.anyString())).thenReturn(span);
//        Mockito.when(span.start()).thenReturn(span);
//        Mockito.when(tracer.withSpanInScope(span)).thenReturn(ws);
    }

    @Test
    void register_thenThrowResponseStatusException() {
        Response.StatusType statusInfo = Response.Status.INTERNAL_SERVER_ERROR;
        var e = Assertions.assertThrows(ResponseStatusException.class,
                () -> {

                    Mockito.when(userValidation.isUsernameExisted(registerAccountRequest.getUsername())).thenReturn(false);
                    Mockito.when(userValidation.isEmailExisted(registerAccountRequest.getEmail())).thenReturn(false);

                    Mockito.when(keycloakAminClient.searchGroupByName(SecurityConst.GROUP_CUSTOMER)).thenReturn(Optional.of(customerGroup));
                    Mockito.when(keycloakAminClient.createUser(Mockito.any())).thenReturn(response);

                    Mockito.when(response.getLocation()).thenReturn(location);
                    Mockito.when(response.getStatusInfo()).thenReturn(statusInfo);
                    Mockito.when(response.getStatus()).thenReturn(statusInfo.getStatusCode());

                    authService.register(registerAccountRequest);
                });

        var expected = "Create method returned status " + statusInfo.getReasonPhrase()
                + " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)";

        Assertions.assertEquals(expected, e.getReason());
    }

    @Test
    void register_givenUsernameExisted_thenThrowAppValidationException() {
        Assertions.assertThrows(AppValidationException.class,
                () -> {

                    Mockito.when(userValidation.isUsernameExisted(registerAccountRequest.getUsername())).thenReturn(true);

                    authService.register(registerAccountRequest);
                });
    }

    @Test
    void register_givenEmailExisted_thenThrowAppValidationException() {
        Assertions.assertThrows(AppValidationException.class,
                () -> {
                    Mockito.when(userValidation.isEmailExisted(registerAccountRequest.getEmail())).thenReturn(true);

                    authService.register(registerAccountRequest);
                });
    }

    @Test
    void register_thenReturnSuccess() throws AppException {

        Mockito.when(userValidation.isUsernameExisted(registerAccountRequest.getUsername())).thenReturn(false);
        Mockito.when(userValidation.isEmailExisted(registerAccountRequest.getEmail())).thenReturn(false);

        Mockito.when(keycloakAminClient.searchGroupByName(SecurityConst.GROUP_CUSTOMER)).thenReturn(Optional.of(customerGroup));
        Mockito.when(keycloakAminClient.createUser(Mockito.any())).thenReturn(response);

        Mockito.when(response.getLocation()).thenReturn(location);
        Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);
        Mockito.when(location.getPath()).thenReturn(RandomStringUtils.random(255));

        var res = "account";
        Mockito.when(messageSource.getMessage(BaseMessageConst.MGS_RES_ACCOUNT, null, LocaleContextHolder.getLocale())).thenReturn(res);
        var msg = "account has been created successfully";
        Mockito.when(messageSource.getMessage(BaseMessageConst.MSG_INF_RESOURCE_CREATED, new String[]{res}, LocaleContextHolder.getLocale())).thenReturn(msg);

        var expected = MessageResponse.builder()
                .message(StringUtils.capitalize(msg))
                .build();

        var actual = authService.register(registerAccountRequest);

        Assertions.assertEquals(expected, actual);
    }

}