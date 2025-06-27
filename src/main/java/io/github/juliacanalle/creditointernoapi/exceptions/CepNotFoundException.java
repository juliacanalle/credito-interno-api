package io.github.juliacanalle.creditointernoapi.exceptions;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException() {
        super("O CEP informado não é válido. Verifique as informações e tente novamente.");
    }
}

