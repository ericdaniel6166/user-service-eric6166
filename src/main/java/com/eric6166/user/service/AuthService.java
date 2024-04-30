package com.eric6166.user.service;

import com.eric6166.base.dto.MessageResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.user.dto.RegisterAccountRequest;

public interface AuthService {
    MessageResponse register(RegisterAccountRequest request) throws AppException;

}
