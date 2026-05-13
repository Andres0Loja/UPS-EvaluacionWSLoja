package ec.edu.ups.evaluacionwsloja.controller;

import ec.edu.ups.evaluacionwsloja.dto.TituloResponse;
import ec.edu.ups.evaluacionwsloja.service.TituloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/titulos")
public class TituloController {

    private final TituloService tituloService;

    public TituloController(TituloService tituloService) {
        this.tituloService = tituloService;
    }

    @GetMapping("/{id}")
    public TituloResponse findById(@PathVariable Long id) {
        return tituloService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tituloService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
