package io.github.juliacanalle.creditointernoapi.exceptions;

public class InactiveEmpresaException extends RuntimeException {
    public InactiveEmpresaException(String cnpj) {
        super("Empresa com CNPJ " + cnpj + " está inativA.");
    }
}
