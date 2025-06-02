package io.github.juliacanalle.creditointernoapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
public class Endereco {

    String logradouro;
    String numero;
    String complemento;
    String cep;
    String bairro;
    String cidade;
    String uf;

    public Endereco(DadosEndereco endereco) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public Endereco() {

    }

}
