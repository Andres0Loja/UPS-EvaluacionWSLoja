package ec.edu.ups.evaluacionwsloja.service;

import ec.edu.ups.evaluacionwsloja.dto.PersonaResponse;
import ec.edu.ups.evaluacionwsloja.dto.TituloResponse;
import ec.edu.ups.evaluacionwsloja.entity.Persona;
import ec.edu.ups.evaluacionwsloja.entity.Titulo;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaResponse toResponse(Persona persona) {
        return new PersonaResponse(
                persona.getCedula(),
                persona.getNombre(),
                persona.getTelefono(),
                persona.getTitulos().size()
        );
    }

    public TituloResponse toResponse(Titulo titulo) {
        Persona persona = titulo.getPersona();
        return new TituloResponse(
                titulo.getId(),
                titulo.getNombre(),
                titulo.getUniversidad(),
                titulo.getFechaRegistro(),
                persona.getCedula(),
                persona.getNombre()
        );
    }
}
