package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ColaboradorRequest(

        @NotBlank
        String nome,

        @NotBlank(message = "CPF não pode estar em branco")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos numéricos.")
        String cpf,

        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
        String cep,

        @NotBlank
        @Min(1)
        String numero,
        String complemento) {
}
