package com.eric6166.user.controller;

import brave.Span;
import brave.Tracer;
import com.eric6166.base.utils.BaseUtils;
import com.eric6166.common.exception.AppException;
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
    BaseUtils baseUtils;
    Tracer tracer;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("test admin");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/feign")
    public ResponseEntity<Object> testFeign(@RequestParam String service) throws AppException {
        log.info("TestController.testFeign");
        return ResponseEntity.ok(testService.testFeign(service));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/kafka")
    public ResponseEntity<Object> testKafka(@RequestParam String service) throws AppException {
        log.info("TestController.testKafka");
        return ResponseEntity.ok(testService.testKafka(service));
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
//    @CircuitBreaker(name = "default", fallbackMethod = "testResilience4jFallbackMethod")
    @CircuitBreaker(name = "default")
    @GetMapping("/resilience4j")
    public ResponseEntity<Object> testResilience4j(@RequestParam String service) throws AppException {
        log.info("TestController.testResilience4j");
        return ResponseEntity.ok(testService.testFeign(service));
    }

    public ResponseEntity<Object> testResilience4jFallbackMethod(String service, RuntimeException exception) {
        log.info("TestController.testResilience4jFallbackMethod");
        Span span = tracer.nextSpan().name("defaultFallbackMethod").start();
        try (var ws = tracer.withSpanInScope(span)) {
            span.tag("request", service);
            span.tag("exception class", exception.getClass().getName());
            span.tag("exception message", exception.getMessage());
            return baseUtils.buildInternalServerErrorResponseExceptionEntity(exception);
        } catch (RuntimeException e) {
            span.error(e);
            return baseUtils.buildInternalServerErrorResponseExceptionEntity(e);
        } finally {
            span.finish();
        }
    }

}
