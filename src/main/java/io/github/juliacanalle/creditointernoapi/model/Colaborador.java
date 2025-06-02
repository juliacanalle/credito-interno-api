package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String nome;
    String cpf;

    @Embedded
    Endereco endereco;

    @Embedded
    Conta conta;

    public Colaborador(String nome, String cpf, DadosEndereco endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = new Endereco(endereco);
    }


}
