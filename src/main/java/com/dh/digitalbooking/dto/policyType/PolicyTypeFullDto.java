package com.dh.digitalbooking.dto.policyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PolicyTypeFullDto(
        @NotNull(message = "Policy type ID is required")
        Long id,
        @NotBlank(message = "Policy type name required")
        @Size(max = 255, message = "Policy type name cannot be longer than 255 characters")
        String name
){
}
