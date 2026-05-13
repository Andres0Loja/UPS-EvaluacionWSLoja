package ec.edu.ups.evaluacionwsloja.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @Column(name = "cedula", length = 10, nullable = false, updatable = false)
    private String cedula;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaRegistro DESC, id DESC")
    private List<Titulo> titulos = new ArrayList<>();

    protected Persona() {
    }

    public Persona(String cedula, String nombre, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Titulo> getTitulos() {
        return titulos;
    }
}
