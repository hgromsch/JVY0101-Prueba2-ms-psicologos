package cl.respirarysoltar.psicologos.api;

import cl.respirarysoltar.psicologos.api.dto.DisponibilidadRequest;
import cl.respirarysoltar.psicologos.api.dto.PsicologoRequest;
import cl.respirarysoltar.psicologos.domain.Disponibilidad;
import cl.respirarysoltar.psicologos.domain.ModalidadAtencion;
import cl.respirarysoltar.psicologos.domain.Psicologo;
import cl.respirarysoltar.psicologos.service.PsicologoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * API REST del catálogo de psicólogos: búsqueda por especialidad/modalidad (RF-02)
 * y publicación de disponibilidad semanal (RF-03).
 */
@RestController
@RequestMapping("/api/psicologos")
public class PsicologoController {

    private final PsicologoService service;

    public PsicologoController(PsicologoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Psicologo> crear(@Valid @RequestBody PsicologoRequest request) {
        Psicologo creado = service.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    /** Lista el catálogo; permite filtrar por especialidad y/o modalidad (RF-02). */
    @GetMapping
    public List<Psicologo> buscar(@RequestParam(required = false) String especialidad,
                                  @RequestParam(required = false) ModalidadAtencion modalidad) {
        return service.buscar(especialidad, modalidad);
    }

    @GetMapping("/{id}")
    public Psicologo obtener(@PathVariable UUID id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public Psicologo actualizar(@PathVariable UUID id, @Valid @RequestBody PsicologoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disponibilidades")
    public ResponseEntity<Disponibilidad> agregarDisponibilidad(@PathVariable UUID id,
                                                                @Valid @RequestBody DisponibilidadRequest request) {
        Disponibilidad creada = service.agregarDisponibilidad(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}/disponibilidades")
    public List<Disponibilidad> listarDisponibilidades(@PathVariable UUID id) {
        return service.listarDisponibilidades(id);
    }

    @DeleteMapping("/{id}/disponibilidades/{disponibilidadId}")
    public ResponseEntity<Void> eliminarDisponibilidad(@PathVariable UUID id, @PathVariable UUID disponibilidadId) {
        service.eliminarDisponibilidad(id, disponibilidadId);
        return ResponseEntity.noContent().build();
    }
}
