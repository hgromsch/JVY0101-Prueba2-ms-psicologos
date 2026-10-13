package cl.respirarysoltar.psicologos.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** Datos de entrada para registrar un bloque de disponibilidad semanal. */
public record DisponibilidadRequest(
        @NotNull(message = "El día de la semana es obligatorio (MONDAY a SUNDAY)")
        DayOfWeek diaSemana,

        @NotNull(message = "La hora de inicio es obligatoria (formato HH:mm)")
        LocalTime horaInicio,

        @NotNull(message = "La hora de término es obligatoria (formato HH:mm)")
        LocalTime horaFin
) {
}
