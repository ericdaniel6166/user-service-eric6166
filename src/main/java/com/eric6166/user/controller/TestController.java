package com.eric6166.user.controller;

import brave.Span;
import brave.Tracer;
import com.eric6166.common.exception.AppException;
import com.eric6166.common.exception.AppExceptionUtils;
import com.eric6166.common.utils.TestConst;
import com.eric6166.user.service.TestService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping("/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    TestService testService;
    AppExceptionUtils appExceptionUtils;
    Tracer tracer;

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
            return appExceptionUtils.buildInternalServerErrorResponseExceptionEntity(exception);
        } catch (RuntimeException e) {
            span.error(e);
            return appExceptionUtils.buildInternalServerErrorResponseExceptionEntity(e);
        } finally {
            span.finish();
        }
    }

}
