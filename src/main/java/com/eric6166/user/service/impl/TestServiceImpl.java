package com.eric6166.user.service.impl;

import brave.Span;
import brave.Tracer;
import com.eric6166.common.config.kafka.AppEvent;
import com.eric6166.common.exception.AppException;
import com.eric6166.security.utils.AppSecurityUtils;
import com.eric6166.user.config.feign.InventoryClient;
import com.eric6166.user.config.kafka.KafkaProducerProps;
import com.eric6166.user.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<Object> testKafka(String service) throws AppException {
        log.info("TestServiceImpl.testKafka");
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
            log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
            span.tag("exception class", e.getClass().getName());
            span.tag("exception message", e.getMessage());
            span.error(e);
            throw new AppException(e);
        } finally {
            span.finish();
        }
    }

    @Override
    public String testFeign(String service) throws AppException {
        log.info("TestServiceImpl.testFeign");
        Span span = tracer.nextSpan().name("testFeign").start();
        try (var ws = tracer.withSpanInScope(span)) {
            String response;
            span.annotate("inventoryClient.productTest Start");
            switch (service) {
                case "inventory" -> response = inventoryClient.productTest(appSecurityUtils.getAuthorizationHeader());
                default -> response = StringUtils.EMPTY;
            }
            span.annotate("inventoryClient.productTest End");
            span.tag("inventoryClient.productTest response", response);
            return response;
        } catch (AppException e) {
            log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.tag("exception class", e.getClass().getName());
            span.tag("exception message", e.getMessage());
            span.error(e);
            throw e;
        } catch (RuntimeException e) {
            log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
            span.tag("exception class", e.getClass().getName());
            span.tag("exception message", e.getMessage());
            span.error(e);
            throw new AppException(e);
        } finally {
            span.finish();
        }

    }

}