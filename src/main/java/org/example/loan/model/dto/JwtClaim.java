package org.example.loan.model.dto;

public record JwtClaim(
        String username,
        String tenantName
) {
}
