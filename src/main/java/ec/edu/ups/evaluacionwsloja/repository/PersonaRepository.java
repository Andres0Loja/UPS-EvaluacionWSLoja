package ec.edu.ups.evaluacionwsloja.repository;

import ec.edu.ups.evaluacionwsloja.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, String> {
}
