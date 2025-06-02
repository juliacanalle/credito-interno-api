package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "enderecos")
@Embeddable
public class Conta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    BigDecimal saldo;

    public Conta(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Conta() {

    }
}
