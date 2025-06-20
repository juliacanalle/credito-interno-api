package io.github.juliacanalle.creditointernoapi.exceptions;

public class CpfAlreadyExistsException extends RuntimeException {
    public CpfAlreadyExistsException(String cpf) {
        super("Esse CPF " + cpf + " já está cadastrado no sistema.");
    }
}
