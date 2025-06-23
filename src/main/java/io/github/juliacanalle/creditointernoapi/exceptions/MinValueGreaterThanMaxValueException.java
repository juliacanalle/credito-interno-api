package io.github.juliacanalle.creditointernoapi.exceptions;

import java.math.BigDecimal;

public class MinValueGreaterThanMaxValueException extends RuntimeException {

    public MinValueGreaterThanMaxValueException(BigDecimal min, BigDecimal max) {
        super(String.format("O valor mínimo (%.2f) não pode ser maior que o valor máximo (%.2f).",
                min, max));
    }
}

