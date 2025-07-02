package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.DadosAtualizaCadastroEmpresa;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.exceptions.AtLeastOneFieldPresentException;
import io.github.juliacanalle.creditointernoapi.exceptions.CnpjAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.EmpresaAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final CepService cepService;
    private final Validator validator;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository, CepService cepService, Validator validator) {
        this.empresaRepository = empresaRepository;
        this.cepService = cepService;
        this.validator = validator;
    }

    @Transactional
    public Empresa cadastrarEmpresaComBuscaCep(@Valid EmpresaRequest request) {
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

        if (empresaRepository.existsByCnpj(request.cnpj())) {
            throw new EmpresaAlreadyExistsException();
        }

        Empresa empresa = new Empresa();
        empresa.setNome(request.nome());
        empresa.setCnpj(request.cnpj());
        empresa.setEndereco(endereco);
        empresa.setAtivo(true);


        Set<ConstraintViolation<Empresa>> errosAoValidarEmpresa = validator.validate(empresa);
        if (!CollectionUtils.isEmpty(errosAoValidarEmpresa)) {
            throw new ConstraintViolationException("Existem erros nos campos da empresa.", errosAoValidarEmpresa);
        }

        Set<ConstraintViolation<CepDto>> errosAoValidarCep = validator.validate(cepDto);
        if (!CollectionUtils.isEmpty(errosAoValidarCep)) {
            throw new ConstraintViolationException("Erro ao validar CEP.", errosAoValidarCep);
        }

        return empresaRepository.save(empresa);
    }

    @Transactional
    public void atualizarEmpresa(String cnpjAtual, DadosAtualizaCadastroEmpresa dados) {
        var empresa = empresaRepository.findByCnpj(cnpjAtual);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada.");
        }

        if (dados.cnpjNovo() == null && dados.nomeNovo() == null) {
            throw new AtLeastOneFieldPresentException();
        }

        if (dados.cnpjNovo() != null && !dados.cnpjNovo().equals(cnpjAtual)) {
            if (empresaRepository.findByCnpj(dados.cnpjNovo()) != null) {
                throw new CnpjAlreadyExistsException();
            }
            empresaRepository.atualizarCnpj(cnpjAtual, dados.cnpjNovo());
        }

        if (dados.nomeNovo() != null) {
            empresaRepository.atualizarNome(cnpjAtual, dados.nomeNovo());
        }
    }


    public void inativarEmpresa (String cnpjAtual){
        Empresa empresa = empresaRepository.findByCnpj(cnpjAtual);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada.");
        }
        empresaRepository.inativarEmpresa(cnpjAtual);
    }
}


