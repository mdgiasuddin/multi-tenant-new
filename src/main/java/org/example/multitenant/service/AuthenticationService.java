package org.example.multitenant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multitenant.config.datasource.TenantContext;
import org.example.multitenant.config.security.JwtService;
import org.example.multitenant.model.dto.request.LoginRequest;
import org.example.multitenant.model.dto.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        TenantContext.setCurrentTenant(request.tenantName());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        return new LoginResponse(jwtService.generateAccessToken(request.username(), request.tenantName()));
    }
}
