package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizaCadastroEmpresa(

        String nomeNovo,
        String cnpjNovo) {
}
