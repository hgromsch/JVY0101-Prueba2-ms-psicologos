package cl.respirarysoltar.psicologos.repository;

import cl.respirarysoltar.psicologos.domain.ModalidadAtencion;
import cl.respirarysoltar.psicologos.domain.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PsicologoRepository extends JpaRepository<Psicologo, UUID> {

    List<Psicologo> findByEspecialidadContainingIgnoreCase(String especialidad);

    List<Psicologo> findByModalidad(ModalidadAtencion modalidad);

    List<Psicologo> findByEspecialidadContainingIgnoreCaseAndModalidad(String especialidad, ModalidadAtencion modalidad);
}
