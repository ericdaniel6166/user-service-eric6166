package com.eric6166.user.controller;

import com.eric6166.base.exception.AppException;
import com.eric6166.base.utils.TestConst;
import com.eric6166.user.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping("/guest")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GuestController {
    TestService testService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("guest test");
    }

    @GetMapping("/test/feign")
    public ResponseEntity<Object> testFeign(@RequestParam(defaultValue = TestConst.INVENTORY, required = false) String service,
                                            @RequestParam(defaultValue = TestConst.PRODUCT_TEST, required = false) String method) throws AppException {
        log.debug("TestController.testFeign");
        return ResponseEntity.ok(testService.testFeign(service, method));
    }

}
