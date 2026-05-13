package ec.edu.ups.evaluacionwsloja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PersonaCreateRequest(
        @NotBlank(message = "La cedula es obligatoria")
        @Size(min = 10, max = 10, message = "La cedula debe tener 10 caracteres")
        String cedula,

        @NotBlank(message = "El nombre de la persona es obligatorio")
        String nombre,

        String telefono
) {
}
