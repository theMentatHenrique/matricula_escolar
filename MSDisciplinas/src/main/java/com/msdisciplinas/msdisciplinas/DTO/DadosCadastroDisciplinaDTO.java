package com.msdisciplinas.msdisciplinas.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroDisciplinaDTO(

        @Min(100L)
        long codigo_disciplina,
        String nome,

        @Pattern(regexp = "[A-G]")
        String horario,

        @Min(100L)
        long cod_turma
) {
}