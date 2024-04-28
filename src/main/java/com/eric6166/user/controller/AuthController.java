package com.eric6166.user.controller;

import com.eric6166.base.dto.AppResponse;
import com.eric6166.common.dto.MessageResponse;
import com.eric6166.common.exception.AppValidationException;
import com.eric6166.user.dto.RegisterAccountRequest;
import com.eric6166.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse<MessageResponse>> register(@RequestBody @Valid RegisterAccountRequest request) throws AppValidationException {
        log.info("AuthController.register"); // comment // for local testing
        return ResponseEntity.ok(new AppResponse<>(authService.register(request)));
    }

    @GetMapping("/test/feign")
    public ResponseEntity<String> testFeign(@RequestParam String service) {
        return ResponseEntity.ok(authService.testFeign(service));
    }

}
