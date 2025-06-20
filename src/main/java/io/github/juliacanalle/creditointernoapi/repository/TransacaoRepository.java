package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByConta(Conta conta);

    Page<Transacao> findByContaAndDataHoraBetweenAndValorBetween(
            Conta conta,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Pageable pageable
    );
}
