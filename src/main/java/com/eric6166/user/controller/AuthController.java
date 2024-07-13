package com.eric6166.user.controller;

import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.user.dto.GetTokenRequest;
import com.eric6166.user.dto.RegisterAccountRequest;
import com.eric6166.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<Object> test() throws AppException, IOException {
        log.info("AuthController.test"); // comment // for local testing
        return ResponseEntity.ok(new AppResponse<>(authService.test()));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterAccountRequest request) throws AppException {
        log.info("AuthController.register"); // comment // for local testing
        return ResponseEntity.ok(new AppResponse<>("register success"));
    }

    @PostMapping("/token")
    public ResponseEntity<Object> getToken(@RequestBody @Valid GetTokenRequest request) throws AppException, IOException {
        log.info("AuthController.register"); // comment // for local testing
        return ResponseEntity.ok(new AppResponse<>(authService.getToken(request)));
    }


}
