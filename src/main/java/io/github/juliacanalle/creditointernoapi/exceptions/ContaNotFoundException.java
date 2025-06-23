package io.github.juliacanalle.creditointernoapi.exceptions;

public class ContaNotFoundException extends RuntimeException {
    public ContaNotFoundException(String cnpj, String cpf) {
        super(String.format("Conta não encontrada para o CNPJ %s e CPF %s.", cnpj, cpf));
    }
}
