package ec.edu.ups.evaluacionwsloja.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiDocumentationController {

    @GetMapping("/api")
    public Map<String, Object> api() {
        return Map.of(
                "name", "UPS Evaluacion WS Loja API",
                "endpoints", List.of(
                        endpoint("POST", "/api/personas", "Crear una persona"),
                        endpoint("GET", "/api/personas", "Listar personas"),
                        endpoint("GET", "/api/personas/{cedula}", "Consultar una persona por cedula"),
                        endpoint("PUT", "/api/personas/{cedula}", "Actualizar datos de una persona"),
                        endpoint("POST", "/api/personas/{cedula}/titulos", "Registrar un titulo para una persona"),
                        endpoint("GET", "/api/personas/{cedula}/titulos", "Listar titulos de una persona"),
                        endpoint("GET", "/api/titulos/{id}", "Consultar un titulo por ID"),
                        endpoint("DELETE", "/api/titulos/{id}", "Eliminar un titulo por ID")
                )
        );
    }

    private Map<String, String> endpoint(String method, String path, String description) {
        return Map.of("method", method, "path", path, "description", description);
    }
}
