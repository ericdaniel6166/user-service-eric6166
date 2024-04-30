package com.eric6166.user.service;

import com.eric6166.base.exception.AppException;

import java.util.List;

public interface TestService {

    Object testFeign(String service, String method, String... params) throws AppException;

    List<Object> testKafka(String service) throws AppException;

}
