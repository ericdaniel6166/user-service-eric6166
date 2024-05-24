package com.eric6166.user.service;

import com.eric6166.base.exception.AppException;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
import com.eric6166.user.dto.TestPostFormRequest;
import com.eric6166.user.dto.TestPostRequest;
import com.eric6166.user.dto.TestS3ObjectRequest;
import com.eric6166.user.dto.TestSqsBatchDeleteRequest;
import com.eric6166.user.dto.TestSqsBatchRequest;
import com.eric6166.user.dto.TestSqsRequest;
import com.eric6166.user.dto.TestUploadRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TestService {

    Object testFeign(String service, String method, String... params) throws AppException;

    List<Object> testKafka(String service) throws AppException;

    void testUpload(TestUploadRequest request);

    Object isBucketExistedBucket(String bucket) throws AppException;

    Object createBucket(TestAWSRequest request) throws AppException;

    Object deleteBucket(TestAWSRequest request) throws AppException;

    Object uploadObject(TestAWSUploadRequest request) throws IOException, AppException;

    Object deleteObject(TestAWSUploadRequest request) throws AppException;

    Object listObject(String bucket) throws AppException;

    Object getObject(String bucket, String key) throws IOException, AppException;

    Object createQueue(TestSqsRequest request) throws AppException;

    Object getQueueUrl(String queueName) throws AppException;

    Object deleteQueue(TestSqsRequest request) throws AppException;

    Object sendMessage(TestSqsRequest request) throws AppException;

    Object sendBatchMessage(TestSqsBatchRequest request) throws AppException;

    List<Map<String, String>> receiveMessage(String queueName, Integer maxNumberOfMessages) throws AppException;

    Object processMessage(TestSqsBatchDeleteRequest request) throws AppException;

    Object presignGetObject(TestS3ObjectRequest request) throws AppException;

    Object copyObject(TestS3ObjectRequest request) throws AppException;

    Object testPost(TestPostRequest request);

    Object testPostForm(TestPostFormRequest request);
}
