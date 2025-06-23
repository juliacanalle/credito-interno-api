package io.github.juliacanalle.creditointernoapi.exceptions;

public class InvalidSortFieldException extends RuntimeException {
    public InvalidSortFieldException(String campoRecebido) {
        super(String.format("Ordenação pelo campo '%s' não é permitida. Os campos válidos são: 'valor' ou 'dataHora'.",
                campoRecebido));
    }
}
