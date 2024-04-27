package com.eric6166.user.validation;

import com.eric6166.common.dto.AccountDto;
import com.eric6166.common.exception.AppValidationException;

public interface UserValidation {
    void validateAccountExisted(AccountDto request) throws AppValidationException;
}
