package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findByContaAndCriadoEmBetweenAndValorBetween(
            Conta conta,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Pageable pageable
    );
}
