package ec.edu.ups.evaluacionwsloja.dto;

public record PersonaResponse(
        String cedula,
        String nombre,
        String telefono,
        int totalTitulos
) {
}
