package io.github.juliacanalle.creditointernoapi.exceptions;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException (String cnpj) {
        super("Empresa com CNPJ " + cnpj + " não encontrada.");
    }
}
