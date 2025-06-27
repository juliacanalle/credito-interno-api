package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizaNomeRequest(
        @NotBlank String nome) {
}
