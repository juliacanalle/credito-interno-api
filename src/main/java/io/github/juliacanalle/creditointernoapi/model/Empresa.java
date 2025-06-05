package io.github.juliacanalle.creditointernoapi.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empresa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String nome;
    String cnpj;
    Endereco endereco;

    public Empresa(String nome, String cnpj, Endereco endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    public Empresa() {

    }
}
