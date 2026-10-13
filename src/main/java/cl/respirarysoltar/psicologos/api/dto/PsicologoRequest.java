package cl.respirarysoltar.psicologos.api.dto;

import cl.respirarysoltar.psicologos.domain.ModalidadAtencion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos de entrada para crear o actualizar un psicólogo del catálogo. */
public record PsicologoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120)
        String nombre,

        @NotBlank(message = "La especialidad es obligatoria")
        @Size(max = 80)
        String especialidad,

        @NotNull(message = "La modalidad es obligatoria (ONLINE, PRESENCIAL o HIBRIDA)")
        ModalidadAtencion modalidad
) {
}
