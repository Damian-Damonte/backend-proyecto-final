package com.dh.digitalbooking.dto.city;

import com.dh.digitalbooking.dto.country.CountryFullDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CityFullDto(
        @NotNull(message = "City ID is required")
        Long id,
        @NotBlank(message = "City name is required")
        @Size(max = 100, message = "City name cannot be longer than 100 characters")
        String name,
        @NotNull(message = "Country ID is required") @Valid
        CountryFullDto country
) {
}
