package io.github.juliacanalle.creditointernoapi.exceptions;

public class ColaboradorNotInCompanyException extends RuntimeException {
    public ColaboradorNotInCompanyException(String cpf, String cnpj) {
        super("Colaborador com CPF " + cpf + " não pertence à empresa com CNPJ" + cnpj + ".");
    }
}
