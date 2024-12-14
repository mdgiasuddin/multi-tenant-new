package org.example.multitenant.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multitenant.config.datasource.TenantContext;
import org.example.multitenant.model.dto.JwtClaim;
import org.example.multitenant.service.LogUtil;
import org.example.multitenant.service.UserService;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.multitenant.constant.AppConstant.BEARER;
import static org.example.multitenant.constant.AppConstant.LOGIN_URI;
import static org.example.multitenant.constant.AppConstant.X_TENANT_NAME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        MDC.put("traceId", LogUtil.getTraceId());
        log.info("API path: {}", requestURI);

        if (requestURI.equals(LOGIN_URI)) {
            String tenantName = request.getHeader(X_TENANT_NAME);
            ;
            TenantContext.setCurrentTenant(tenantName);
            MDC.put("tenantName", tenantName);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = authHeader.substring(BEARER.length());
        JwtClaim claim = jwtService.extractUserDetails(accessToken);
        TenantContext.setCurrentTenant(claim.tenantName());
        MDC.put("tenantName", claim.tenantName());
        UserDetails userDetails = userService.getUserByUsername(claim.username());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
