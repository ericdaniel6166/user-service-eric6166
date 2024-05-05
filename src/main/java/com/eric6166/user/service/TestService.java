package com.eric6166.user.service;

import com.eric6166.base.exception.AppException;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
import com.eric6166.user.dto.TestSqsRequest;
import com.eric6166.user.dto.TestUploadRequest;

import java.io.IOException;
import java.util.List;

public interface TestService {

    Object testFeign(String service, String method, String... params) throws AppException;

    List<Object> testKafka(String service) throws AppException;

    void testUpload(TestUploadRequest request);

    Object isBucketExistedBucket(String bucket);

    Object createBucket(TestAWSRequest request) throws AppException;

    Object deleteBucket(TestAWSRequest request) throws AppException;

    Object uploadObject(TestAWSUploadRequest request) throws IOException, AppException;

    Object deleteObject(TestAWSUploadRequest request) throws AppException;

    Object listObject(String bucket);

    Object getObject(String bucket, String key) throws IOException;

    Object createQueue(TestSqsRequest request);

    Object getQueueUrl(String queueName);
}
