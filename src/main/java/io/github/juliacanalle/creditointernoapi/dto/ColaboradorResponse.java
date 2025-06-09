package io.github.juliacanalle.creditointernoapi.dto;

import java.math.BigDecimal;

public record ColaboradorResponse(
        Long id,
        String nome,
        String cpf,
        EnderecoResponse endereco,
        ContaResponse conta,
        EmpresaResponse empresa
) {
    public ColaboradorResponse(io.github.juliacanalle.creditointernoapi.model.Colaborador c) {
        this(
                c.getId(),
                c.getNome(),
                c.getCpf(),
                new EnderecoResponse(c.getEndereco()),
                new ContaResponse(c.getConta()),
                new EmpresaResponse(c.getEmpresa())
        );
    }

    public record EnderecoResponse(
            String cep,
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String localidade,
            String uf
    ) {
        public EnderecoResponse(io.github.juliacanalle.creditointernoapi.model.Endereco e) {
            this(
                    e.getCep(),
                    e.getLogradouro(),
                    e.getNumero(),
                    e.getComplemento(),
                    e.getBairro(),
                    e.getLocalidade(),
                    e.getUf()
            );
        }
    }

    public record ContaResponse(
            Long id,
            BigDecimal saldo
    ) {
        public ContaResponse(io.github.juliacanalle.creditointernoapi.model.Conta c) {
            this(
                    c.getId(),
                    c.getSaldo()
            );
        }
    }
}
