package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(

        @NotBlank
        String logradouro,

        @NotBlank
        String bairro,

        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
        String cep,

        @NotBlank
        String localidade,

        @NotBlank
        String uf,

        String complemento,

        @NotBlank
        String numero) {
}
