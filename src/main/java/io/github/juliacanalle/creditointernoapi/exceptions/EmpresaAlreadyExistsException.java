package io.github.juliacanalle.creditointernoapi.exceptions;

public class EmpresaAlreadyExistsException extends RuntimeException {
    public EmpresaAlreadyExistsException() {
        super("Já existe uma empresa com esse CNPJ cadastrada no sistema.");
    }
}
