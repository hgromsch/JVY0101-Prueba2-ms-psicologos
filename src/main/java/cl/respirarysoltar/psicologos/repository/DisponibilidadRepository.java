package cl.respirarysoltar.psicologos.repository;

import cl.respirarysoltar.psicologos.domain.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, UUID> {

    List<Disponibilidad> findByPsicologoId(UUID psicologoId);
}
