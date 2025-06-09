package io.github.juliacanalle.creditointernoapi.dto;

public record ColaboradorRequest(

        String nome,
        String cpf,
        String cep,
        String numero,
        String complemento) {
}
