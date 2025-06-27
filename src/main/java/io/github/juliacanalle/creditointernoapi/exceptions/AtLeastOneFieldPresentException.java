package io.github.juliacanalle.creditointernoapi.exceptions;

public class AtLeastOneFieldPresentException extends RuntimeException {
    public AtLeastOneFieldPresentException() {
        super("Pelo menos um dos campos devem ser preenchidos, informe um novo nome ou um novo cnpj, ou os dois podem ser atualizados.");
    }
}
