package ec.edu.ups.evaluacionwsloja.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "titulos")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "universidad", nullable = false, length = 160)
    private String universidad;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_cedula", nullable = false)
    private Persona persona;

    protected Titulo() {
    }

    public Titulo(String nombre, String universidad, LocalDate fechaRegistro, Persona persona) {
        this.nombre = nombre;
        this.universidad = universidad;
        this.fechaRegistro = fechaRegistro;
        this.persona = persona;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUniversidad() {
        return universidad;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public Persona getPersona() {
        return persona;
    }
}
