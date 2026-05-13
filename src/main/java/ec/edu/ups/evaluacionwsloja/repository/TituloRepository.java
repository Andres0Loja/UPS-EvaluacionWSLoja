package ec.edu.ups.evaluacionwsloja.repository;

import ec.edu.ups.evaluacionwsloja.entity.Titulo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloRepository extends JpaRepository<Titulo, Long> {

    List<Titulo> findByPersonaCedulaOrderByFechaRegistroDescIdDesc(String cedula);
}
