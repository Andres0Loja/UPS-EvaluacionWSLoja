package ec.edu.ups.evaluacionwsloja.service;

import ec.edu.ups.evaluacionwsloja.dto.TituloCreateRequest;
import ec.edu.ups.evaluacionwsloja.dto.TituloResponse;
import ec.edu.ups.evaluacionwsloja.entity.Persona;
import ec.edu.ups.evaluacionwsloja.entity.Titulo;
import ec.edu.ups.evaluacionwsloja.exception.ResourceNotFoundException;
import ec.edu.ups.evaluacionwsloja.repository.TituloRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TituloService {

    private final TituloRepository tituloRepository;
    private final PersonaService personaService;
    private final PersonaMapper mapper;

    public TituloService(TituloRepository tituloRepository, PersonaService personaService, PersonaMapper mapper) {
        this.tituloRepository = tituloRepository;
        this.personaService = personaService;
        this.mapper = mapper;
    }

    @Transactional
    public TituloResponse createForPersona(String cedula, TituloCreateRequest request) {
        Persona persona = personaService.getPersonaOrThrow(cedula);
        Titulo titulo = new Titulo(
                request.nombre().trim(),
                request.universidad().trim(),
                LocalDate.now(),
                persona
        );
        return mapper.toResponse(tituloRepository.save(titulo));
    }

    @Transactional(readOnly = true)
    public List<TituloResponse> findByPersona(String cedula) {
        personaService.getPersonaOrThrow(cedula);
        return tituloRepository.findByPersonaCedulaOrderByFechaRegistroDescIdDesc(cedula).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TituloResponse findById(Long id) {
        Titulo titulo = tituloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un titulo con id " + id));
        return mapper.toResponse(titulo);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!tituloRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un titulo con id " + id);
        }
        tituloRepository.deleteById(id);
    }
}
