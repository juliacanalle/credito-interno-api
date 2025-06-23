package io.github.juliacanalle.creditointernoapi.exceptions;

import java.time.LocalDate;

public class DataRangeExceedLimitException extends RuntimeException {
    public DataRangeExceedLimitException(LocalDate dataInicio, LocalDate dataFim) {
        super(String.format("O intervalo de datas entre %s e %s não pode ser maior que 30 dias.",
                dataInicio, dataFim));
    }
}
