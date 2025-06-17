package io.github.juliacanalle.creditointernoapi.dto;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
                                Long id,
                                LocalDateTime criadoEm,
                                TipoTransacao tipo,
                                BigDecimal valor,
                                BigDecimal saldo,
                                String mensagem
) {
    public TransacaoResponse(Transacao t) {
        this(
                t.getId(),
                t.getCriadoEm(),
                t.getTipoTransacao(),
                t.getValor(),
                t.getSaldo(),
                t.getMensagem()
        );
    }
}
