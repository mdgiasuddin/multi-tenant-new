package org.example.multitenant.model.dto;

public record JwtClaim(
        String username,
        String tenantName
) {
}
