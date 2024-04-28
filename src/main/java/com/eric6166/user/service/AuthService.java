package com.eric6166.user.service;

import com.eric6166.common.dto.MessageResponse;
import com.eric6166.common.exception.AppException;
import com.eric6166.user.dto.RegisterAccountRequest;

import java.util.List;

public interface AuthService {
    MessageResponse register(RegisterAccountRequest request) throws AppException;

    String testFeign(String service) throws AppException;

    List<Object> testKafka(String service) throws AppException;
}
