package cl.respirarysoltar.psicologos.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Psicólogo del catálogo consultable por los pacientes (RF-02, RF-03).
 * Su disponibilidad semanal se modela en la entidad relacionada Disponibilidad.
 */
@Entity
@Table(name = "psicologos")
public class Psicologo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String especialidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private ModalidadAtencion modalidad;

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    /** Relación OneToMany: bloques semanales en que el psicólogo atiende. */
    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilidad> disponibilidades = new ArrayList<>();

    protected Psicologo() {
        // Constructor requerido por JPA
    }

    public Psicologo(String nombre, String especialidad, ModalidadAtencion modalidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.modalidad = modalidad;
    }

    @PrePersist
    void alCrear() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
    }

    public void agregarDisponibilidad(Disponibilidad disponibilidad) {
        disponibilidad.setPsicologo(this);
        disponibilidades.add(disponibilidad);
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public ModalidadAtencion getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadAtencion modalidad) {
        this.modalidad = modalidad;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }
}
