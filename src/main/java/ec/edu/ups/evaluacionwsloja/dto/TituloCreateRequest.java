package ec.edu.ups.evaluacionwsloja.dto;

import jakarta.validation.constraints.NotBlank;

public record TituloCreateRequest(
        @NotBlank(message = "El nombre del titulo es obligatorio")
        String nombre,

        @NotBlank(message = "La universidad es obligatoria")
        String universidad
) {
}
