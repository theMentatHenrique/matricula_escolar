package com.msalunos.msalunos.DTO;

import jakarta.validation.constraints.*;

public record DadosCadastroAlunoDTO(
        @NotEmpty
        String nome,

        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")
        String num_documento,
        @NotEmpty
        String endereco) {
}
