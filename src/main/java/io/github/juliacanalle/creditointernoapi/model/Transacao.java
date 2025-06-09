package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
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

    @Column(nullable = false)
    private String tipo;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
