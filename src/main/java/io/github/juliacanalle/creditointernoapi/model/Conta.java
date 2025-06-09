package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "contas")
@Data
public class Conta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal saldo;

    public Conta(BigDecimal saldo) {
        this.saldo = BigDecimal.ZERO;
    }

    public Conta() {

    }
}
