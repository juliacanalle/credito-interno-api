package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequest(

        String nome,
        String cnpj,
        Boolean ativo,
        String cep,
        String numero,
        String complemento) {
}
