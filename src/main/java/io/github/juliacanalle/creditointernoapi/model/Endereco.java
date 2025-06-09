package io.github.juliacanalle.creditointernoapi.model;

import io.github.juliacanalle.creditointernoapi.dto.DadosEndereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
public class Endereco {
    @Column(nullable = false, unique = true)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String logradouro;

    @NotBlank
    @Column(nullable = false)
    private String numero;

    private String complemento;

    @NotBlank
    @Column(nullable = false)
    private String cep;

    @NotBlank
    @Column(nullable = false)
    private String bairro;

    @NotBlank
    @Column(nullable = false)
    private String localidade;

    @NotBlank
    @Column(nullable = false)
    private String uf;

    public Endereco(DadosEndereco endereco) {
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.cep = endereco.cep();
        this.bairro = endereco.bairro();
        this.localidade = endereco.localidade();
        this.uf = endereco.uf();
    }

    public Endereco() {

    }
}
