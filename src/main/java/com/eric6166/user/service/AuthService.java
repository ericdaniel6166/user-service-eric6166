package com.eric6166.user.service;

import com.eric6166.common.dto.MessageResponse;
import com.eric6166.user.dto.RegisterAccountRequest;

public interface AuthService {
    //    MessageResponse register(RegisterAccountRequest request) throws AppValidationException;
    MessageResponse register(RegisterAccountRequest request);
}