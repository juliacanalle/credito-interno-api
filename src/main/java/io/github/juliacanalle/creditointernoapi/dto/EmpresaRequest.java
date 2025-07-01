package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmpresaRequest(

        @NotBlank
        @Min(3)
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{14}")
        String cnpj,

        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos.")
        String cep,

        @NotBlank
        @Min(1)
        String numero,

        String complemento) {
}
