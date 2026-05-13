package ec.edu.ups.evaluacionwsloja.dto;

import java.time.LocalDate;

public record TituloResponse(
        Long id,
        String nombre,
        String universidad,
        LocalDate fechaRegistro,
        String personaCedula,
        String personaNombre
) {
}
