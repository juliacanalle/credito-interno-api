package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.DadosAtualizaCadastroEmpresa;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.exceptions.AtLeastOneFieldPresentException;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final CepService cepService;
    private Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository, CepService cepService) {
        this.empresaRepository = empresaRepository;
        this.cepService = cepService;
    }

    @Transactional
    public Empresa cadastrarEmpresaComBuscaCep (@Valid EmpresaRequest request) {
        CepDto cepDto = cepService.consultaCep(request.cep());

        validator.validate(cepDto);

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

        validator.validate(empresa);

        return empresaRepository.save(empresa);
    }

    @Transactional
    public void atualizarEmpresa(String cnpjAtual, DadosAtualizaCadastroEmpresa dados) {
        var empresa = empresaRepository.findByCnpj(cnpjAtual);
        if(empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada.");
        }
        if (!cnpjAtual.equals(dados.cnpjNovo())) {
            var outraEmpresa = empresaRepository.findByCnpj(dados.cnpjNovo());
            if (outraEmpresa != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe empresa cadastrada com esse CNPJ.");
            }
        }
        if (dados.cnpjNovo() != null) {
            empresaRepository.atualizarCnpj(cnpjAtual, dados.cnpjNovo());
        }
        if (dados.nomeNovo() != null) {
            empresaRepository.atualizarNome(cnpjAtual, dados.nomeNovo());
        }
        if (dados.cnpjNovo() == null && dados.nomeNovo() == null) {
            throw new AtLeastOneFieldPresentException();
        }
        empresaRepository.save(empresa);
    }
}

