package io.github.juliacanalle.creditointernoapi.exceptions;

public class InactiveColaboradorException extends RuntimeException {
    public InactiveColaboradorException(String cpf) {
        super("Colaborador com CPF " + cpf + " está inativo.");
    }
}
