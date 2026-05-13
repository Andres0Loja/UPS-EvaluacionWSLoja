package ec.edu.ups.evaluacionwsloja.service;

import ec.edu.ups.evaluacionwsloja.dto.PersonaCreateRequest;
import ec.edu.ups.evaluacionwsloja.dto.PersonaResponse;
import ec.edu.ups.evaluacionwsloja.dto.PersonaUpdateRequest;
import ec.edu.ups.evaluacionwsloja.entity.Persona;
import ec.edu.ups.evaluacionwsloja.exception.DuplicateResourceException;
import ec.edu.ups.evaluacionwsloja.exception.ResourceNotFoundException;
import ec.edu.ups.evaluacionwsloja.repository.PersonaRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper mapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper mapper) {
        this.personaRepository = personaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PersonaResponse create(PersonaCreateRequest request) {
        if (personaRepository.existsById(request.cedula())) {
            throw new DuplicateResourceException("Ya existe una persona con cedula " + request.cedula());
        }

        Persona persona = new Persona(
                request.cedula(),
                request.nombre().trim(),
                normalizeOptional(request.telefono())
        );
        return mapper.toResponse(personaRepository.save(persona));
    }

    @Transactional(readOnly = true)
    public PersonaResponse findByCedula(String cedula) {
        return mapper.toResponse(getPersonaOrThrow(cedula));
    }

    @Transactional(readOnly = true)
    public List<PersonaResponse> findAll() {
        return personaRepository.findAll().stream()
                .sorted(Comparator.comparing(Persona::getNombre))
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public PersonaResponse update(String cedula, PersonaUpdateRequest request) {
        Persona persona = getPersonaOrThrow(cedula);
        persona.setNombre(request.nombre().trim());
        persona.setTelefono(normalizeOptional(request.telefono()));
        return mapper.toResponse(persona);
    }

    @Transactional(readOnly = true)
    public Persona getPersonaOrThrow(String cedula) {
        return personaRepository.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una persona con cedula " + cedula));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
