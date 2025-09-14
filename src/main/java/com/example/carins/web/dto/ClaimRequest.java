package com.example.carins.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ClaimRequest(
        @NotNull(message = "Claim date is required") LocalDate claimDate,
        @NotNull(message = "Description is required") String description,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive") Double amount
) {}
