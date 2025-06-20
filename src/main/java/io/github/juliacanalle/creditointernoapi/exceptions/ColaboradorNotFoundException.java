package io.github.juliacanalle.creditointernoapi.exceptions;

public class ColaboradorNotFoundException extends RuntimeException {
    public ColaboradorNotFoundException(String cpf) {
        super("Colaborador com CPF " + cpf + " não encontrado.");
    }
}