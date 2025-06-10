package io.github.juliacanalle.creditointernoapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OperacaoRequest(

        @NotNull
        @Positive
        BigDecimal valor) {
}
