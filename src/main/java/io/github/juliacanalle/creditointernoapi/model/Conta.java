package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "contas")
@Data
public class Conta {

    private static final BigDecimal LIMITE_CREDITO = BigDecimal.valueOf(10_000);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal saldo = BigDecimal.ZERO;

    public Conta() {
    }

    public Conta(BigDecimal saldo) {
        this.saldo = BigDecimal.ZERO;
    }

    public void creditar(BigDecimal valor) {
        BigDecimal limite = new BigDecimal("10000");
        if (valor.compareTo(BigDecimal.ZERO) <= 0
                && valor.compareTo(LIMITE_CREDITO) > 0) {
            throw new IllegalArgumentException("Para realizar um depósito o valor deve estar entre 0 e " + LIMITE_CREDITO + ".");
        }
<<<<<<< Updated upstream
        this.saldo = this.saldo.add(valor);
    }

    public void debitar(BigDecimal valor) {
        BigDecimal limite = new BigDecimal("10000");
        if (valor.compareTo(BigDecimal.ZERO) <= 0
                && valor.compareTo(LIMITE_CREDITO) > 0) {
            throw new IllegalArgumentException("Para debitar, o valor deve estar entre 0 e " + LIMITE_CREDITO + ".");
        }
        this.saldo = this.saldo.subtract(valor);
    }
}

=======
    }
>>>>>>> Stashed changes

