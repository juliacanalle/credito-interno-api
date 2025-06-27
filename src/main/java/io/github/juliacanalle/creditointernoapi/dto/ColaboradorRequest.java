package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ColaboradorRequest(

        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "12")
        String cpf,

        @NotBlank
        @Pattern(regexp = "8")
        String cep,

        @NotBlank
        @Min(1)
        String numero,
        String complemento) {
}
