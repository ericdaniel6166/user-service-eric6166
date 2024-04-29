package com.eric6166.user.service;

import com.eric6166.common.exception.AppException;

import java.util.List;

public interface TestService {

    String testFeign(String service) throws AppException;

    List<Object> testKafka(String service) throws AppException;
}
