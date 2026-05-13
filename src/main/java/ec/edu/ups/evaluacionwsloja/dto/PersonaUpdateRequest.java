package ec.edu.ups.evaluacionwsloja.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonaUpdateRequest(
        @NotBlank(message = "El nombre de la persona es obligatorio")
        String nombre,

        String telefono
) {
}
