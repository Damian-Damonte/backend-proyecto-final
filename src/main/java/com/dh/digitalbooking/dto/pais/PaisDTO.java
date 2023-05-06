package com.dh.digitalbooking.dto.pais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaisDTO (
        @NotNull(message = "Debe enviar el id del pais")
        Long id,
        @NotBlank(message = "El pais debe contener un nombre")
        @Size(max = 45, message = "El nombre del pais no debe tener mas de 45 caracteres")
        String nombre){
}
