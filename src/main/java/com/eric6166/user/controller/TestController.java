package com.eric6166.user.controller;

import brave.Span;
import brave.Tracer;
import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppExceptionUtils;
import com.eric6166.base.utils.BaseUtils;
import com.eric6166.base.utils.TestConst;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
import com.eric6166.user.dto.TestSqsRequest;
import com.eric6166.user.dto.TestUploadRequest;
import com.eric6166.user.service.TestService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@Validated
@RequestMapping("/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    TestService testService;
    AppExceptionUtils appExceptionUtils;
    BaseUtils baseUtils;
    Tracer tracer;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/sqs/queue")
    public ResponseEntity<Object> createQueue(@RequestBody TestSqsRequest request) throws AppException {
        log.debug("TestController.createBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.createQueue(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/sqs/queue")
    public ResponseEntity<Object> getQueueUrl(@RequestParam String queueName) throws AppException {
        log.debug("TestController.createBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.getQueueUrl(queueName)));
    }



    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> isBucketExistedBucket(@RequestParam String bucket) {
        log.debug("TestController.isBucketExistedBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.isBucketExistedBucket(bucket)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> createBucket(@RequestBody TestAWSRequest request) throws AppException {
        log.debug("TestController.createBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.createBucket(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> deleteBucket(@RequestBody TestAWSRequest request) throws AppException {
        log.debug("TestController.deleteBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.deleteBucket(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/object", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadObject(@ModelAttribute TestAWSUploadRequest request) throws IOException, AppException {
        log.debug("TestController.uploadObject");
        return ResponseEntity.ok(new AppResponse<>(testService.uploadObject(request)));
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/s3/object")
    public ResponseEntity<Object> getObject(@RequestParam String bucket, @RequestParam(required = false) String key) throws IOException {
        log.debug("TestController.object");
        if (StringUtils.isBlank(key)) {
            return ResponseEntity.ok(new AppResponse<>(testService.listObject(bucket)));
        }

        return ResponseEntity.ok(new AppResponse<>(testService.getObject(bucket, key)));
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/aws/s3/object")
    public ResponseEntity<Object> deleteObject(@RequestBody TestAWSUploadRequest request) throws AppException {
        log.debug("TestController.deleteObject");
        return ResponseEntity.ok(testService.deleteObject(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> testUpload(@Valid @ModelAttribute TestUploadRequest request) {
        log.debug("TestController.testUpload");
        testService.testUpload(request);
        return ResponseEntity.ok("test upload");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("test admin");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/feign")
    public ResponseEntity<Object> testFeign(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
                                            @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method,
                                            @RequestParam(name = TestConst.FIELD_PARAM, required = false) String... params) throws AppException {
        log.debug("TestController.testFeign");
        return ResponseEntity.ok(testService.testFeign(service, method, params));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/kafka")
    public ResponseEntity<Object> testKafka(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service) throws AppException {
        log.debug("TestController.testKafka");
        return ResponseEntity.ok(testService.testKafka(service));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
//    @CircuitBreaker(name = "default", fallbackMethod = "testResilience4jFallbackMethod")
    @CircuitBreaker(name = "default")
    @GetMapping("/resilience4j")
    public ResponseEntity<Object> testResilience4j(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
                                                   @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method) throws AppException {
        log.debug("TestController.testResilience4j");
        return ResponseEntity.ok(testService.testFeign(service, method));
    }

    public ResponseEntity<Object> testResilience4jFallbackMethod(String service, RuntimeException exception) {
        log.debug("TestController.testResilience4jFallbackMethod");
        Span span = tracer.nextSpan().name("defaultFallbackMethod").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.error(exception);
            span.tag("service", service);
            var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } catch (RuntimeException e) {
            span.error(e);
            var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
            return baseUtils.buildResponseExceptionEntity(errorResponse);
        } finally {
            span.finish();
        }
    }

}
