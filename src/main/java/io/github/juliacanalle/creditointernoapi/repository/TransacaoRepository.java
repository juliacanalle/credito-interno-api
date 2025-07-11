package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findByContaAndCriadoEmBetweenAndValorBetween(
            Conta conta,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Pageable pageable
    );

    List<Transacao> findByContaAndCriadoEmBetweenAndValorBetween(
            Conta conta,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Sort sort
    );

    @Query("""
    SELECT SUM(t.valor)
    FROM Transacao t
    WHERE t.tipoTransacao = :tipo
      AND (
        :cnpj IS NULL OR 
        t.conta.colaborador.empresa.cnpj = :cnpj
      )
""")
    BigDecimal somaDeCreditosLiberados(
            @Param("tipo") TipoTransacao tipo,
            @Param("cnpj") String cnpj
    );

}
