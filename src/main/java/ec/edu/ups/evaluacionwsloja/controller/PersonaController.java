package ec.edu.ups.evaluacionwsloja.controller;

import ec.edu.ups.evaluacionwsloja.dto.PersonaCreateRequest;
import ec.edu.ups.evaluacionwsloja.dto.PersonaResponse;
import ec.edu.ups.evaluacionwsloja.dto.PersonaUpdateRequest;
import ec.edu.ups.evaluacionwsloja.dto.TituloCreateRequest;
import ec.edu.ups.evaluacionwsloja.dto.TituloResponse;
import ec.edu.ups.evaluacionwsloja.service.PersonaService;
import ec.edu.ups.evaluacionwsloja.service.TituloService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;
    private final TituloService tituloService;

    public PersonaController(PersonaService personaService, TituloService tituloService) {
        this.personaService = personaService;
        this.tituloService = tituloService;
    }

    @PostMapping
    public ResponseEntity<PersonaResponse> create(@Valid @RequestBody PersonaCreateRequest request) {
        PersonaResponse response = personaService.create(request);
        return ResponseEntity.created(URI.create("/api/personas/" + response.cedula())).body(response);
    }

    @GetMapping("/{cedula}")
    public PersonaResponse findByCedula(@PathVariable String cedula) {
        return personaService.findByCedula(cedula);
    }

    @GetMapping
    public List<PersonaResponse> findAll() {
        return personaService.findAll();
    }

    @PutMapping("/{cedula}")
    public PersonaResponse update(@PathVariable String cedula, @Valid @RequestBody PersonaUpdateRequest request) {
        return personaService.update(cedula, request);
    }

    @PostMapping("/{cedula}/titulos")
    public ResponseEntity<TituloResponse> createTitulo(
            @PathVariable String cedula,
            @Valid @RequestBody TituloCreateRequest request
    ) {
        TituloResponse response = tituloService.createForPersona(cedula, request);
        return ResponseEntity.created(URI.create("/api/titulos/" + response.id())).body(response);
    }

    @GetMapping("/{cedula}/titulos")
    public List<TituloResponse> findTitulos(@PathVariable String cedula) {
        return tituloService.findByPersona(cedula);
    }
}
