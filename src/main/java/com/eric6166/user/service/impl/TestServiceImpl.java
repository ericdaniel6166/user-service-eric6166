package com.eric6166.user.service.impl;

import brave.Span;
import brave.Tracer;
import com.eric6166.aws.service.S3Service;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppExceptionUtils;
import com.eric6166.base.utils.TestConst;
import com.eric6166.common.config.kafka.AppEvent;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.user.config.feign.InventoryClient;
import com.eric6166.user.config.kafka.KafkaProducerProps;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
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

import java.io.IOException;
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

    @Override
    public Object isBucketExistedBucket(String bucket) {
        log.debug("TestServiceImpl.isBucketExistedBucket");
        boolean isBucketExisted = s3Service.isBucketExisted(bucket);
        Map<String, Object> response = new HashMap<>();
        response.put("isBucketExisted", isBucketExisted);
        return response;
    }

    @Override
    public Object createBucket(TestAWSRequest request) throws AppException {
        s3Service.createBucket(request.getBucket());
        return "OK";
    }

    @Override
    public Object deleteBucket(TestAWSRequest request) throws AppException {
        s3Service.deleteBucket(request.getBucket());
        return "OK";
    }

    @Override
    public Object uploadObject(TestAWSUploadRequest request) throws IOException, AppException {
        s3Service.uploadObject(request.getBucket(), request.getKey(), request.getFile());
        return "OK";
    }

    @Override
    public Object deleteObject(TestAWSUploadRequest request) throws AppException {
        s3Service.deleteObject(request.getBucket(), request.getKey());
        return "OK";
    }


    @Override
    public void testUpload(TestUploadRequest request) {
        log.debug("TestServiceImpl.testUpload");
    }

    @Override
    public List<Object> testKafka(String service) throws AppException {
        log.debug("TestServiceImpl.testKafka");
        Span span = tracer.nextSpan().name("testKafka").start();
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
            span.tag("exception.class", e.getClass().getName());
            span.error(e);
            throw new AppException(e);
        } finally {
            span.finish();
        }
    }

    @Override
    public Object testFeign(String service, String method, String... params) throws AppException {
        log.debug("TestServiceImpl.testFeign");
        Span span = tracer.nextSpan().name("testFeign").start();
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
        } catch (AppException e) {
            log.debug("e: {} , rootCause: {}", e.getClass().getName(), AppExceptionUtils.getAppExceptionRootCause(e)); // comment // for local testing
            span.tag("exception.class", e.getClass().getName());
            span.tag("exception.rootCause", AppExceptionUtils.getAppExceptionRootCause(e).toString());
            span.error(e);
            throw e;
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
            span.tag("exception.class", e.getClass().getName());
            span.error(e);
            throw new AppException(e);
        } finally {
            span.finish();
        }

    }

}