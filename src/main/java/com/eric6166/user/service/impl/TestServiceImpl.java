package com.eric6166.user.service.impl;

import brave.Tracer;
import com.eric6166.aws.s3.S3Service;
import com.eric6166.aws.sqs.SqsService;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.TestConst;
import com.eric6166.common.config.kafka.AppEvent;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.user.config.feign.InventoryClient;
import com.eric6166.user.config.kafka.KafkaProducerProps;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
import com.eric6166.user.dto.TestS3ObjectRequest;
import com.eric6166.user.dto.TestSqsBatchDeleteRequest;
import com.eric6166.user.dto.TestSqsBatchRequest;
import com.eric6166.user.dto.TestSqsRequest;
import com.eric6166.user.dto.TestUploadRequest;
import com.eric6166.user.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchResponse;
import software.amazon.awssdk.services.sqs.model.DeleteQueueResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestServiceImpl implements TestService {

    InventoryClient inventoryClient;
    AppSecurityUtils appSecurityUtils;
    KafkaTemplate<String, Object> kafkaTemplate;
    KafkaProducerProps kafkaProducerProps;
    Tracer tracer;
    S3Service s3Service;
    SqsService sqsService;

    @Override
    public Object isBucketExistedBucket(String bucket) {
        boolean isBucketExisted = s3Service.isBucketExisted(bucket);
        Map<String, Object> response = new HashMap<>();
        response.put("isBucketExisted", isBucketExisted);
        return response;
    }

    @Override
    public Object createBucket(TestAWSRequest request) throws AppException {
        var o = s3Service.createBucket(request.getBucket());
        Map<String, Object> response = new HashMap<>();
        response.put("location", o.location());
        return response;
    }

    @Override
    public Object deleteBucket(TestAWSRequest request) throws AppException {
        var o = s3Service.deleteBucket(request.getBucket());
        Map<String, Object> response = new HashMap<>();
        return response;
    }

    @Override
    public Object uploadObject(TestAWSUploadRequest request) throws IOException, AppException {
        var o = s3Service.uploadObject(request.getBucket(), request.getKey(), request.getFile());
        Map<String, Object> response = new HashMap<>();
        response.put("eTag", o.eTag());
        return response;
    }

    @Override
    public Object deleteObject(TestAWSUploadRequest request) throws AppException {
        var o = s3Service.deleteObject(request.getBucket(), request.getKey());
        Map<String, Object> response = new HashMap<>();
        response.put("deleteMarker", o.deleteMarker());
        return response;
    }

    @Override
    public Object listObject(String bucket) throws AppException {
        var o = s3Service.listObject(bucket);
        Map<String, Object> response = new HashMap<>();
        return o.contents().stream().map(i -> {
            Map<String, Object> m = new HashMap<>();
            m.put("key", i.key());
            m.put("eTag", i.eTag());
//            m.put("lastModified", LocalDateTime.ofInstant(i.lastModified(), BaseConst.DEFAULT_ZONE_ID));
//            m.put("owner.id", i.owner().id());
//            m.put("owner.displayName", i.owner().displayName());
//            m.put("restoreStatus", i.restoreStatus().toString());
//            m.put("restoreStatus.isRestoreInProgress", i.restoreStatus().isRestoreInProgress());
//            m.put("restoreStatus.restoreExpiryDate", LocalDateTime.ofInstant(i.restoreStatus().restoreExpiryDate(), BaseConst.DEFAULT_ZONE_ID));
            return m;
        }).toList();
//        return response;
    }

    @Override
    public Object getObject(String bucket, String key) throws IOException, AppException {
        var o = s3Service.getObject(bucket, key);
        var o1 = s3Service.getObjectAsBytes(bucket, key); //
//        String text = IOUtils.toString(o1.asInputStream(), StandardCharsets.UTF_8.name()); // if file is text, etc
//        File targetFile = new File("src/main/resources/test.jpg");
//        FileUtils.copyInputStreamToFile(o1.asInputStream(), targetFile);

        Map<String, Object> response = new HashMap<>();
        response.put("acceptRanges", o.response().acceptRanges());
        response.put("contentLength", o.response().contentLength());
        response.put("eTag", o.response().eTag());
//        response.put("expires", LocalDateTime.ofInstant(o.response().expires(), BaseConst.DEFAULT_ZONE_ID));
        response.put("hasMetadata", o.response().hasMetadata());
//        response.put("lastModified", LocalDateTime.ofInstant(o.response().lastModified(), BaseConst.DEFAULT_ZONE_ID));
        response.put("missingMeta", o.response().missingMeta());
        response.put("metadata", o.response().metadata());
        response.put("restore", o.response().restore());
//        response.put("serverSideEncryption", o.response().serverSideEncryption().toString());
//        response.put("serverSideEncryption.name", o.response().serverSideEncryption().name());
//        response.put("storageClass", o.response().storageClass().toString());
//        response.put("storageClass.name", o.response().storageClass().name());
        response.put("tagCount", o.response().tagCount());

        return response;

    }

    @Override
    public Object createQueue(TestSqsRequest request) throws AppException {
        var o = sqsService.createQueue(request.getQueueName());
        Map<String, Object> response = new HashMap<>();
        response.put("queueUrl", o.queueUrl());
        return response;
    }

    @Override
    public Object getQueueUrl(String queueName) throws AppException {
        var o = sqsService.getQueueUrl(queueName);
        Map<String, Object> response = new HashMap<>();
        response.put("queueUrl", o.queueUrl());
        return response;
    }

    @Override
    public Object deleteQueue(TestSqsRequest request) throws AppException {
        DeleteQueueResponse o;
        if (StringUtils.isNotBlank(request.getQueueUrl())) {
            o = sqsService.deleteQueueByQueueUrl(request.getQueueUrl());

        } else if (StringUtils.isNotBlank(request.getQueueName())) {
            o = sqsService.deleteQueueByQueueName(request.getQueueName());
        }
        Map<String, Object> response = new HashMap<>();
        return response;
    }

    @Override
    public Object sendMessage(TestSqsRequest request) throws AppException {
        var o = sqsService.sendMessageByQueueName(request.getQueueName(), request.getMessage(), request.getDelaySeconds(), request.getMessageGroupId());
        Map<String, Object> response = new HashMap<>();
        response.put("messageId", o.messageId());
        response.put("sequenceNumber", o.sequenceNumber());
        return response;
    }

    @Override
    public Object sendBatchMessage(TestSqsBatchRequest request) throws AppException {
        request.setMessages(request.getMessages().stream().map(i -> {
            i.setId(UUID.randomUUID().toString());
            return i;
        }).toList());
        var o = sqsService.sendBatchMessageByQueueName(request.getQueueName(), request);
        Map<String, Object> response = new HashMap<>();
        response.put("hasSuccessful", o.hasSuccessful());
        response.put("hasFailed", o.hasFailed());
        response.put("failed.size", o.failed().size());
        return response;
    }

    @Override
    public List<Map<String, String>> receiveMessage(String queueName, Integer maxNumberOfMessages) throws AppException {
        var o = sqsService.receiveMessageByQueueName(queueName, maxNumberOfMessages);
        return o.messages().stream().map(i -> {
            Map<String, String> m = new HashMap<>();
            m.put("body", i.body());
            m.put("messageId", i.messageId());
            m.put("receiptHandle", i.receiptHandle());
            return m;
        }).toList();
    }

    @Override
    public Object processMessage(TestSqsBatchDeleteRequest request) throws AppException {
        List<Map<String, String>> receiveMessage = receiveMessage(request.getQueueName(), request.getMaxNumberOfMessages());
        if (receiveMessage.size() > 0) {
            request.setMessages(receiveMessage.stream().map(i -> {
                var m = new TestSqsBatchDeleteRequest.Message();
                m.setReceiptHandle(i.get("receiptHandle"));
                m.setId(i.get("messageId"));
                return m;
            }).toList());
            Map<String, Object> response = new HashMap<>();
            DeleteMessageBatchResponse o = sqsService.deleteMessageBatchByQueueName(request.getQueueName(), request);
            response.put("hasSuccessful", o.hasSuccessful());
            response.put("hasFailed", o.hasFailed());
            response.put("failed.size", o.failed().size());
            response.put("messages", receiveMessage);
            return response;
        }
        return "No Messages Available";

    }

    @Override
    public Object copyObject(TestS3ObjectRequest request) throws AppException {
        var o = s3Service.copyObject(request.getSourceBucket(), request.getSourceKey(), request.getDestinationBucket(), request.getDestinationKey());
        Map<String, Object> r = new HashMap<>();
        r.put("copyObjectResult.eTag", o.copyObjectResult().eTag());
        r.put("copyObjectResult.lastModified", LocalDateTime.ofInstant(o.copyObjectResult().lastModified(), BaseConst.DEFAULT_ZONE_ID).toString());
        r.put("serverSideEncryption.name", o.serverSideEncryption().name());

        return r;
    }

    @Override
    public Object presignGetObject(TestS3ObjectRequest request) throws AppException {
        var signatureDuration = request.getSignatureDuration() == null ? null : Duration.ofMinutes(request.getSignatureDuration());
        var o = s3Service.presignGetObject(request.getBucket(), request.getKey(), signatureDuration);
        Map<String, Object> r = new HashMap<>();
        r.put("url", o.url().toString());
        r.put("isBrowserExecutable", o.isBrowserExecutable());
        return r;
    }

    //    create queue
//    getQueueUrl
//            o.queueUrl(); //https://sqs.ap-southeast-1.amazonaws.com/891377091766/eric6166-default-test
//            o.queueUrl(); //https://sqs.ap-southeast-1.amazonaws.com/891377091766/eric6166-default-test.fifo

    //listObject
//        o.name(); //bucket //eric6166-test
//        o.keyCount(); // // 2 objs -> 2 keys
//
//        o.contents().get(0);
//        o.contents().get(0).key();
//        o.contents().get(0).lastModified();
//        o.contents().get(0).eTag();
//        o.contents().get(0).size();
//        o.contents().get(0).storageClass(); //ObjectStorageClass.STANDARD

    //getobj
//        String content = IOUtils.toString(o, StandardCharsets.UTF_8);
//        o.response().eTag();
//        o.response().contentLength();
//        o.response().lastModified();
//        o.response().serverSideEncryption();
//        o.response().contentType();
//        o.response().acceptRanges();

    //upload obj
//        o.eTag()
//        o.serverSideEncryption()

    //create bucket
//        response.put("location", o.location());

    //general, delete obj, delete bucket
//        response.put("extendedRequestId", o.responseMetadata().extendedRequestId());
//        response.put("requestId", o.responseMetadata().requestId());

    @Override
    public void testUpload(TestUploadRequest request) {
        log.debug("TestServiceImpl.testUpload");
    }

    @Override
    public List<Object> testKafka(String service) throws AppException {
        log.debug("TestServiceImpl.testKafka");
        var span = tracer.nextSpan().name("testKafka").start();
        try (var ws = tracer.withSpanInScope(span)) {
            var messageToDefaultTopic = String.format("topic: %s, message from: user service, to: %s service", kafkaProducerProps.getDefaultTopicName(), service);
            var defaultTopicEvent = AppEvent.builder()
                    .payload(messageToDefaultTopic)
                    .uuid(UUID.randomUUID().toString())
                    .build();
            span.tag("defaultTopicEvent uuid", defaultTopicEvent.getUuid());
            kafkaTemplate.send(kafkaProducerProps.getDefaultTopicName(),
                    defaultTopicEvent);
            span.annotate("defaultTopicEvent sent");
            var messageToTestTopic = String.format("topic: %s, message from: user service, to: %s service", kafkaProducerProps.getTestTopicName(), service);
            var testTopicAppEvent = AppEvent.builder()
                    .payload(messageToTestTopic)
                    .uuid(UUID.randomUUID().toString())
                    .build();
            span.tag("testTopicAppEvent uuid", testTopicAppEvent.getUuid());
            kafkaTemplate.send(kafkaProducerProps.getTestTopicName(),
                    testTopicAppEvent);
            span.annotate("testTopicAppEvent sent");
            return List.of(defaultTopicEvent, testTopicAppEvent);
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    @Override
    public Object testFeign(String service, String method, String... params) throws AppException {
        log.debug("TestServiceImpl.testFeign");
        var span = tracer.nextSpan().name("testFeign").start();
        try (var ws = tracer.withSpanInScope(span)) {
            Object response;
            span.annotate(String.format("%sClient.%s Start", service, method));
            switch (service) {
                case TestConst.INVENTORY -> {
                    switch (method) {
                        case TestConst.PRODUCT_TEST -> response = inventoryClient.productTest(appSecurityUtils.getAuthorizationHeader());
                        case TestConst.PRODUCT_FIND_ALL -> {
                            Integer pageNumber = NumberUtils.toInt(params[0], 1);
                            Integer pageSize = NumberUtils.toInt(params[1], 1);
                            String sortColumn = StringUtils.defaultString(params[2], null);
                            String sortDirection = StringUtils.defaultString(params[3], null);
                            response = inventoryClient.productFindAll(appSecurityUtils.getAuthorizationHeader(),
                                    pageNumber, pageSize, sortColumn, sortDirection);
                        }

                        case TestConst.ADMIN_TEST -> response = inventoryClient.adminTest(appSecurityUtils.getAuthorizationHeader());
                        default -> response = StringUtils.EMPTY;
                    }
                }
                default -> response = StringUtils.EMPTY;
            }
            span.annotate(String.format("%sClient.%s End", service, method));
            span.tag(String.format("%sClient.%s response", service, method), response.toString());
            return response;
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }

    }

}