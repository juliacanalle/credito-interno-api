package io.github.juliacanalle.creditointernoapi.exceptions;

public class CnpjAlreadyExistsException extends RuntimeException {
    public CnpjAlreadyExistsException() {
        super("O novo CNPJ não pode ser igual ao atual.");
    }
}
