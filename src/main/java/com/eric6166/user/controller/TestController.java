package com.eric6166.user.controller;

import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.utils.DateTimeUtils;
import com.eric6166.base.utils.TestConst;
import com.eric6166.base.validation.ValidNumber;
import com.eric6166.user.dto.TestAWSRequest;
import com.eric6166.user.dto.TestAWSUploadRequest;
import com.eric6166.user.dto.TestPostFormRequest;
import com.eric6166.user.dto.TestPostRequest;
import com.eric6166.user.dto.TestResponse;
import com.eric6166.user.dto.TestS3ObjectRequest;
import com.eric6166.user.dto.TestSqsBatchDeleteRequest;
import com.eric6166.user.dto.TestSqsBatchRequest;
import com.eric6166.user.dto.TestSqsRequest;
import com.eric6166.user.dto.TestUploadRequest;
import com.eric6166.user.service.TestService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequestMapping("/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    TestService testService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/sqs/queue")
    public ResponseEntity<Object> createQueue(@RequestBody TestSqsRequest request) throws AppException {
        log.info("TestController.createQueue");
        return ResponseEntity.ok(new AppResponse<>(testService.createQueue(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/sqs/message")
    public ResponseEntity<Object> receiveMessage(@RequestParam String queueName, @RequestParam(required = false) Integer maxNumberOfMessages) throws AppException {
        log.info("TestController.receiveMessage");
        return ResponseEntity.ok(new AppResponse<>(testService.receiveMessage(queueName, maxNumberOfMessages)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/sqs/message/process")
    public ResponseEntity<Object> processMessage(@RequestBody TestSqsBatchDeleteRequest request) throws AppException {
        log.info("TestController.processMessage");
        return ResponseEntity.ok(new AppResponse<>(testService.processMessage(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/sqs/message")
    public ResponseEntity<Object> sendMessage(@RequestBody TestSqsRequest request) throws AppException {
        log.info("TestController.sendMessage");
        return ResponseEntity.ok(new AppResponse<>(testService.sendMessage(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/sqs/batch-message")
    public ResponseEntity<Object> sendBatchMessage(@RequestBody TestSqsBatchRequest request) throws AppException {
        log.info("TestController.sendBatchMessage");
        return ResponseEntity.ok(new AppResponse<>(testService.sendBatchMessage(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/aws/sqs/queue")
    public ResponseEntity<Object> deleteQueue(@RequestBody TestSqsRequest request) throws AppException {
        log.info("TestController.deleteQueue");
        return ResponseEntity.ok(new AppResponse<>(testService.deleteQueue(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/sqs/queue")
    public ResponseEntity<Object> getQueueUrl(@RequestParam String queueName) throws AppException {
        log.info("TestController.getQueueUrl");
        return ResponseEntity.ok(new AppResponse<>(testService.getQueueUrl(queueName)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> isBucketExistedBucket(@RequestParam String bucket) throws AppException {
        log.info("TestController.isBucketExistedBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.isBucketExistedBucket(bucket)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> createBucket(@RequestBody TestAWSRequest request) throws AppException {
        log.info("TestController.createBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.createBucket(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/aws/s3/bucket")
    public ResponseEntity<Object> deleteBucket(@RequestBody TestAWSRequest request) throws AppException {
        log.info("TestController.deleteBucket");
        return ResponseEntity.ok(new AppResponse<>(testService.deleteBucket(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/object", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadObject(@ModelAttribute TestAWSUploadRequest request) throws IOException, AppException {
        log.info("TestController.uploadObject");
        return ResponseEntity.ok(new AppResponse<>(testService.uploadObject(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/object/presign-url")
    public ResponseEntity<Object> presignGetObject(@RequestBody TestS3ObjectRequest request) throws AppException {
        log.info("TestController.uploadObject");
        return ResponseEntity.ok(new AppResponse<>(testService.presignGetObject(request)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/aws/s3/object/copy")
    public ResponseEntity<Object> copyObject(@RequestBody TestS3ObjectRequest request) throws AppException {
        log.info("TestController.uploadObject");
        return ResponseEntity.ok(new AppResponse<>(testService.copyObject(request)));
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/aws/s3/object")
    public ResponseEntity<Object> getObject(@RequestParam String bucket, @RequestParam(required = false) String key) throws IOException, AppException {
        log.info("TestController.object");
        if (StringUtils.isBlank(key)) {
            return ResponseEntity.ok(new AppResponse<>(testService.listObject(bucket)));
        }

        return ResponseEntity.ok(new AppResponse<>(testService.getObject(bucket, key)));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "/aws/s3/object")
    public ResponseEntity<Object> deleteObject(@RequestBody TestAWSUploadRequest request) throws AppException {
        log.info("TestController.deleteObject");
        return ResponseEntity.ok(testService.deleteObject(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> testUpload(@Valid @ModelAttribute TestUploadRequest request) {
        log.info("TestController.testUpload");
        testService.testUpload(request);
        return ResponseEntity.ok("test upload");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/test-post")
    public ResponseEntity<Object> testPost(@Valid @RequestBody TestPostRequest request) {
        log.info("TestController.testPost");
        return ResponseEntity.ok(testService.testPost(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/test-get")
    public ResponseEntity<Object> testGet(@RequestParam String paramStr,
                                          @RequestParam @ValidNumber(flag = ValidNumber.Flag.PARSEABLE) String paramBigDecimal) {
        log.info("TestController.testGet, {}, {}", paramStr, paramBigDecimal);
        return ResponseEntity.ok("test get");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/test-return")
    public ResponseEntity<Object> testReturn(@RequestBody TestPostRequest request) {
        log.info("TestController.testReturn");
        Map<String, Object> m = new HashMap<>();
        request.getZoneIds().forEach(s -> {
            var zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).withZoneSameInstant(ZoneId.of(s));
            m.put(s, DateTimeUtils.toString(zonedDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME));
        });
        var response = TestResponse.builder()
                .zonedDateTimes(m)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/test-post-form", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> testPostForm(@Valid @ModelAttribute TestPostFormRequest request) {
        log.info("TestController.testPostForm");
        return ResponseEntity.ok(testService.testPostForm(request));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("test admin");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/feign")
    public ResponseEntity<Object> testFeign(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
                                            @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method,
                                            @RequestParam(name = TestConst.FIELD_PARAM, required = false) String... params) throws AppException {
        log.info("TestController.testFeign");
        return ResponseEntity.ok(testService.testFeign(service, method, params));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/kafka")
    public ResponseEntity<Object> testKafka(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service) throws AppException {
        log.info("TestController.testKafka");
        return ResponseEntity.ok(testService.testKafka(service));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @CircuitBreaker(name = "default")
    @Retry(name = "default")
//    @TimeLimiter(name = "default")
//    @RateLimiter(name = "default")
//    @Bulkhead(name = "default")
    @GetMapping("/resilience4j")
    public ResponseEntity<Object> testResilience4j(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
                                                   @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method,
                                                   @RequestParam(name = TestConst.FIELD_PARAM, required = false) String... params) throws AppException {
        log.info("TestController.testResilience4j");
        return ResponseEntity.ok(testService.testFeign(service, method, params));
    }

//    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
////    @CircuitBreaker(name = "default", fallbackMethod = "defaultCircuitBreakerFallBackMethod")
//    @CircuitBreaker(name = "default")
//    @Retry(name = "default")
//    @TimeLimiter(name = "default")
////    @RateLimiter(name = "default")
////    @Bulkhead(name = "default")
//    @GetMapping("/resilience4j")
//    public CompletionStage<ResponseEntity<Object>> testResilience4j2(
//            @RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
//            @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method,
//            @RequestParam(name = TestConst.FIELD_PARAM, required = false) String... params) throws Throwable {
//        log.info("TestController.testResilience4j");
//        return CompletableFuture.completedFuture(ResponseEntity.ok(testService.testFeign(service, method, params)));
//
//    }

//    public CompletionStage<ResponseEntity<Object>> defaultCircuitBreakerFallBackMethod(String service,
//                                                                                       String method,
//                                                                                       String[] params,
//                                                                                       CallNotPermittedException e) {
//        log.info("defaultCircuitBreakerFallBackMethod, handle the exception when the CircuitBreaker is open");
//        return CompletableFuture.supplyAsync(() -> BaseUtils.buildFallBackMethodResponseExceptionEntity(
//                AppExceptionUtils.buildFallBackMethodErrorResponse(HttpStatus.SERVICE_UNAVAILABLE)));
//    }
//
//    public CompletionStage<ResponseEntity<Object>> defaultCircuitBreakerFallBackMethod(String service,
//                                                                                    String method,
//                                                                                    String[] params,
//                                                                                    Exception e) {
//        log.info("defaultCircuitBreakerFallBackMethod, handle other exception, e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
//        return CompletableFuture.supplyAsync(() -> BaseUtils.buildFallBackMethodResponseExceptionEntity(
//                AppExceptionUtils.buildFallBackMethodErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR)));
//    }


}
