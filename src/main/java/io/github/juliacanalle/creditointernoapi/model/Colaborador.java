package io.github.juliacanalle.creditointernoapi.model;

import io.github.juliacanalle.creditointernoapi.dto.DadosEndereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Colaborador {

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", nullable = false)
    Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id", nullable = false)
    Conta conta;

    @NotNull
    @Column(nullable = false)
    private boolean ativo = true;

    public Colaborador(String nome, String cpf, DadosEndereco endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = new Endereco(endereco);
    }

    public void inativarColaborador() {
        this.ativo = false;
    }

    public void atualizarNome(String novoNome) {
        this.nome = novoNome;
    }
}
