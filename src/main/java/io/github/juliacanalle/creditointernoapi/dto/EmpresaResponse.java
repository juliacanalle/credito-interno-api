package io.github.juliacanalle.creditointernoapi.dto;

import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;

public record EmpresaResponse(
        Long id,
        String nome,
        String cnpj,
        EnderecoResponse endereco
) {
    public EmpresaResponse(Empresa empresa) {
        this(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                new EnderecoResponse(empresa.getEndereco())
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
        public EnderecoResponse(Endereco e) {
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
}
