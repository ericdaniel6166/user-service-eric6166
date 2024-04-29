package com.eric6166.user.validation.impl;

import com.eric6166.common.dto.AccountDto;
import com.eric6166.common.exception.AppValidationException;
import com.eric6166.common.exception.ValidationErrorDetail;
import com.eric6166.common.utils.Const;
import com.eric6166.common.utils.MessageConstant;
import com.eric6166.keycloak.service.KeycloakService;
import com.eric6166.user.validation.UserValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidationImpl implements UserValidation {


    MessageSource messageSource;
    KeycloakService keycloakService;

    @Override
    public void validateAccountExisted(AccountDto account) throws AppValidationException {
        log.info("UserValidationImpl.validateAccountExisted"); // comment // for local testing
        Set<ValidationErrorDetail> errorDetails = new HashSet<>();
        Optional<UserRepresentation> searchByUsername = keycloakService.searchUserByUsername(account.getUsername());
        if (searchByUsername.isPresent()) {
            String res = messageSource.getMessage(MessageConstant.MGS_RES_USERNAME, null, LocaleContextHolder.getLocale());
            String msg = messageSource.getMessage(MessageConstant.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(Const.FIELD_USERNAME, null, StringUtils.capitalize(msg)));
        }
        Optional<UserRepresentation> searchByEmail = keycloakService.searchUserByEmail(account.getEmail());
        if (searchByEmail.isPresent()) {
            String res = messageSource.getMessage(MessageConstant.MGS_RES_EMAIL, null, LocaleContextHolder.getLocale());
            String msg = messageSource.getMessage(MessageConstant.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(Const.FIELD_EMAIL, null, StringUtils.capitalize(msg)));
        }

        if (CollectionUtils.isNotEmpty(errorDetails)) {
            throw new AppValidationException(new ArrayList<>(errorDetails));
        }


    }
}
