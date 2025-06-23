package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.DadosAtualizaCadastroEmpresa;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final CepService cepService;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository, CepService cepService) {
        this.empresaRepository = empresaRepository;
        this.cepService = cepService;
    }

    @Transactional
    public Empresa cadastrarEmpresaComBuscaCep (@Valid EmpresaRequest request) {
        CepDto cepDto = cepService.consultaCep(request.cep());

        Endereco endereco = new Endereco();
        endereco.setCep(request.cep());
        endereco.setLogradouro(cepDto.logradouro());
        endereco.setBairro(cepDto.bairro());
        endereco.setLocalidade(cepDto.localidade());
        endereco.setUf(cepDto.uf());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());

        Empresa empresa = new Empresa();
        empresa.setNome(request.nome());
        empresa.setCnpj(request.cnpj());
        empresa.setEndereco(endereco);
        empresa.setAtivo(true);

        return empresaRepository.save(empresa);
    }
}

