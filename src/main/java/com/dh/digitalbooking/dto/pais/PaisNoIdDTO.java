package com.dh.digitalbooking.dto.pais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaisNoIdDTO (
        @NotBlank(message = "Country name is required")
        @Size(max = 45, message = "Country name cannot be longer than 45 characters")
        String name){
}