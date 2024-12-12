package org.example.loan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.loan.model.dto.request.LoginRequest;
import org.example.loan.model.dto.response.LoginResponse;
import org.example.loan.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }
}
