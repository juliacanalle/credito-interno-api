package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizaCadastroEmpresa(

        @Pattern(regexp = "^(?!\\s*$).+", message = "Nome não pode ser vazio ou só com espaços.")
        String nomeNovo,
        @Pattern(regexp = "^(?!\\s*$).+", message = "CNPJ não pode ser vazio ou só com espaços.")
        String cnpjNovo) {
}
