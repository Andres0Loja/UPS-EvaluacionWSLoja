package ec.edu.ups.evaluacionwsloja;

import ec.edu.ups.evaluacionwsloja.repository.PersonaRepository;
import ec.edu.ups.evaluacionwsloja.repository.TituloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonaApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TituloRepository tituloRepository;

    @BeforeEach
    void cleanDatabase() {
        tituloRepository.deleteAll();
        personaRepository.deleteAll();
    }

    @Test
    void crearPersona() throws Exception {
        createPersona("1102030405", "Maria Loja")
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/personas/1102030405"))
                .andExpect(jsonPath("$.cedula").value("1102030405"))
                .andExpect(jsonPath("$.nombre").value("Maria Loja"))
                .andExpect(jsonPath("$.totalTitulos").value(0));
    }

    @Test
    void consultarPersonaPorCedula() throws Exception {
        createPersona("1202030405", "Carlos Perez").andExpect(status().isCreated());

        mockMvc.perform(get("/api/personas/{cedula}", "1202030405"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value("1202030405"))
                .andExpect(jsonPath("$.nombre").value("Carlos Perez"));
    }

    @Test
    void registrarTituloParaPersona() throws Exception {
        createPersona("1302030405", "Diana Torres").andExpect(status().isCreated());

        mockMvc.perform(post("/api/personas/{cedula}/titulos", "1302030405")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Medicina",
                                  "universidad": "Universidad Politecnica Salesiana"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Medicina"))
                .andExpect(jsonPath("$.universidad").value("Universidad Politecnica Salesiana"))
                .andExpect(jsonPath("$.personaCedula").value("1302030405"));
    }

    @Test
    void listarTitulosPorCedula() throws Exception {
        createPersona("1402030405", "Elena Maza").andExpect(status().isCreated());
        createTitulo("1402030405", "Arquitectura", "UPS").andExpect(status().isCreated());
        createTitulo("1402030405", "Maestria en Gestion", "UTPL").andExpect(status().isCreated());

        mockMvc.perform(get("/api/personas/{cedula}/titulos", "1402030405"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].personaCedula").value("1402030405"));
    }

    @Test
    void cedulaInvalidaDevuelveBadRequest() throws Exception {
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cedula": "123",
                                  "nombre": "Persona Invalida"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.cedula").value("La cedula debe tener 10 caracteres"));
    }

    @Test
    void jsonMalformadoDevuelveBadRequest() throws Exception {
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{cedula:"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El cuerpo de la solicitud no es JSON valido"));
    }

    private org.springframework.test.web.servlet.ResultActions createPersona(String cedula, String nombre) throws Exception {
        return mockMvc.perform(post("/api/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "cedula": "%s",
                          "nombre": "%s",
                          "telefono": "0999999999"
                        }
                        """.formatted(cedula, nombre)));
    }

    private org.springframework.test.web.servlet.ResultActions createTitulo(String cedula, String nombre, String universidad) throws Exception {
        return mockMvc.perform(post("/api/personas/{cedula}/titulos", cedula)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "nombre": "%s",
                          "universidad": "%s"
                        }
                        """.formatted(nombre, universidad)));
    }
}
