package cl.respirarysoltar.psicologos.service;

import cl.respirarysoltar.psicologos.api.dto.DisponibilidadRequest;
import cl.respirarysoltar.psicologos.api.dto.PsicologoRequest;
import cl.respirarysoltar.psicologos.domain.Disponibilidad;
import cl.respirarysoltar.psicologos.domain.ModalidadAtencion;
import cl.respirarysoltar.psicologos.domain.Psicologo;
import cl.respirarysoltar.psicologos.exception.RecursoNoEncontradoException;
import cl.respirarysoltar.psicologos.exception.ReglaNegocioException;
import cl.respirarysoltar.psicologos.repository.DisponibilidadRepository;
import cl.respirarysoltar.psicologos.repository.PsicologoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio del catálogo de psicólogos (RF-02 búsqueda por especialidad,
 * RF-03 publicación de disponibilidad). ms-agenda consume esta disponibilidad.
 */
@Service
@Transactional
public class PsicologoService {

    private final PsicologoRepository psicologoRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    public PsicologoService(PsicologoRepository psicologoRepository,
                            DisponibilidadRepository disponibilidadRepository) {
        this.psicologoRepository = psicologoRepository;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    public Psicologo crear(PsicologoRequest request) {
        Psicologo psicologo = new Psicologo(request.nombre(), request.especialidad(), request.modalidad());
        return psicologoRepository.save(psicologo);
    }

    @Transactional(readOnly = true)
    public List<Psicologo> buscar(String especialidad, ModalidadAtencion modalidad) {
        boolean hayEspecialidad = especialidad != null && !especialidad.isBlank();
        if (hayEspecialidad && modalidad != null) {
            return psicologoRepository.findByEspecialidadContainingIgnoreCaseAndModalidad(especialidad, modalidad);
        }
        if (hayEspecialidad) {
            return psicologoRepository.findByEspecialidadContainingIgnoreCase(especialidad);
        }
        if (modalidad != null) {
            return psicologoRepository.findByModalidad(modalidad);
        }
        return psicologoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Psicologo obtener(UUID id) {
        return psicologoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Psicólogo no encontrado: " + id));
    }

    public Psicologo actualizar(UUID id, PsicologoRequest request) {
        Psicologo psicologo = obtener(id);
        psicologo.setNombre(request.nombre());
        psicologo.setEspecialidad(request.especialidad());
        psicologo.setModalidad(request.modalidad());
        return psicologoRepository.save(psicologo);
    }

    public void eliminar(UUID id) {
        Psicologo psicologo = obtener(id);
        psicologoRepository.delete(psicologo);
    }

    public Disponibilidad agregarDisponibilidad(UUID psicologoId, DisponibilidadRequest request) {
        if (!request.horaFin().isAfter(request.horaInicio())) {
            throw new ReglaNegocioException("La hora de término debe ser posterior a la hora de inicio");
        }
        Psicologo psicologo = obtener(psicologoId);
        Disponibilidad disponibilidad = new Disponibilidad(request.diaSemana(), request.horaInicio(), request.horaFin());
        psicologo.agregarDisponibilidad(disponibilidad);
        return disponibilidadRepository.save(disponibilidad);
    }

    @Transactional(readOnly = true)
    public List<Disponibilidad> listarDisponibilidades(UUID psicologoId) {
        obtener(psicologoId);
        return disponibilidadRepository.findByPsicologoId(psicologoId);
    }

    public void eliminarDisponibilidad(UUID psicologoId, UUID disponibilidadId) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Disponibilidad no encontrada: " + disponibilidadId));
        if (!disponibilidad.getPsicologo().getId().equals(psicologoId)) {
            throw new ReglaNegocioException("La disponibilidad no pertenece al psicólogo indicado");
        }
        disponibilidadRepository.delete(disponibilidad);
    }
}
