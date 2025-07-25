package io.github.juliacanalle.creditointernoapi.model;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipoTransacao;

    @Column(nullable = false)
    private BigDecimal valor;

    private LocalDateTime criadoEm = LocalDateTime.now();

    public Transacao() {
    }

    public Transacao(Conta conta, BigDecimal saldo, String mensagem, TipoTransacao tipoTransacao, BigDecimal valor, LocalDateTime criadoEm) {
        this.conta = conta;
        this.saldo = saldo;
        this.mensagem = mensagem;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.criadoEm = criadoEm;
    }
}
