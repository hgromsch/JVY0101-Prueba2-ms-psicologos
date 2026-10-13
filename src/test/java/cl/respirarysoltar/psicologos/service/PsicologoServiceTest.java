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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PsicologoServiceTest {

    @Mock
    private PsicologoRepository psicologoRepository;

    @Mock
    private DisponibilidadRepository disponibilidadRepository;

    @InjectMocks
    private PsicologoService service;

    @Test
    void crearGuardaElPsicologo() {
        PsicologoRequest request = new PsicologoRequest("Luis Soto", "Ansiedad", ModalidadAtencion.ONLINE);
        when(psicologoRepository.save(any(Psicologo.class))).thenAnswer(inv -> inv.getArgument(0));

        Psicologo creado = service.crear(request);

        assertNotNull(creado);
        assertEquals("Luis Soto", creado.getNombre());
    }

    @Test
    void obtenerConIdInexistenteLanzaNoEncontrado() {
        UUID id = UUID.randomUUID();
        when(psicologoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtener(id));
    }

    @Test
    void agregarDisponibilidadConRangoInvertidoLanzaReglaNegocio() {
        DisponibilidadRequest request = new DisponibilidadRequest(
                DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(9, 0));

        assertThrows(ReglaNegocioException.class, () -> service.agregarDisponibilidad(UUID.randomUUID(), request));
    }

    @Test
    void agregarDisponibilidadValidaLaGuarda() {
        UUID psicologoId = UUID.randomUUID();
        Psicologo psicologo = new Psicologo("Luis Soto", "Ansiedad", ModalidadAtencion.ONLINE);
        when(psicologoRepository.findById(psicologoId)).thenReturn(Optional.of(psicologo));
        when(disponibilidadRepository.save(any(Disponibilidad.class))).thenAnswer(inv -> inv.getArgument(0));

        DisponibilidadRequest request = new DisponibilidadRequest(
                DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
        Disponibilidad creada = service.agregarDisponibilidad(psicologoId, request);

        assertNotNull(creada);
        assertEquals(psicologo, creada.getPsicologo());
    }
}
