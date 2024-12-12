package org.example.loan.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String tenantName
) {
}
