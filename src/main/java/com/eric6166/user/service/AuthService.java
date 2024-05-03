package com.eric6166.user.service;

import com.eric6166.base.dto.MessageResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.user.dto.GetTokenRequest;
import com.eric6166.user.dto.RegisterAccountRequest;

import java.io.IOException;

public interface AuthService {
    MessageResponse register(RegisterAccountRequest request) throws AppException;

    Object getToken(GetTokenRequest request) throws IOException;

    Object test();
}
