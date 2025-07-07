package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.*;
import io.github.juliacanalle.creditointernoapi.exceptions.ColaboradorNotFoundException;
import io.github.juliacanalle.creditointernoapi.exceptions.CpfAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.EmpresaNotFoundException;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ColaboradorService {

    private final CepService cepService;
    private final EmpresaRepository empresaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ContaService contaService;
    private final Validator validator;

    public ColaboradorService(
            CepService cepService,
            EmpresaRepository empresaRepository,
            ColaboradorRepository colaboradorRepository,
            ContaService contaService, Validator validator)
    {
        this.cepService = cepService;
        this.empresaRepository = empresaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.contaService = contaService;
        this.validator = validator;
    }

    @Transactional
    public Colaborador cadastrarColaboradorComBuscaCep (@Valid ColaboradorRequest request, String cnpj) {
        CepDto cepDto = cepService.consultaCep(request.cep());

        Endereco endereco = new Endereco();
        endereco.setCep(request.cep());
        endereco.setLogradouro(cepDto.logradouro());
        endereco.setBairro(cepDto.bairro());
        endereco.setLocalidade(cepDto.localidade());
        endereco.setUf(cepDto.uf());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());

        if (colaboradorRepository.existsByCpf(request.cpf())) {
            throw new CpfAlreadyExistsException(request.cpf());
        }

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(request.nome());
        colaborador.setCpf(request.cpf());
        colaborador.setEndereco(endereco);

        Conta  conta = new Conta();
        colaborador.setConta(conta);

        Empresa empresa = empresaRepository.findByCnpj(cnpj);
        if (empresa == null) {
            throw new EmpresaNotFoundException(cnpj);
        }
        colaborador.setEmpresa(empresa);

        Set<ConstraintViolation<Colaborador>> errosAoValidarColaborador = validator.validate(colaborador);
        if (!CollectionUtils.isEmpty(errosAoValidarColaborador)) {
            throw new ConstraintViolationException("Existem erros nos dados do colaborador.", errosAoValidarColaborador);
        }

        Set<ConstraintViolation<CepDto>> errosAoValidarCep = validator.validate(cepDto);
        if (!CollectionUtils.isEmpty(errosAoValidarCep)) {
            throw new ConstraintViolationException("Erro ao validar CEP.", errosAoValidarCep);
        }

        return colaboradorRepository.save(colaborador);
    }

    public void atualizarEnderecoColaborador(@Valid AtualizaEnderecoRequest request, String cpf) {
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }

        CepDto cepDto = cepService.consultaCep(request.cep());

        Endereco novoEndereco = new Endereco();
        novoEndereco.setCep(request.cep());
        novoEndereco.setLogradouro(cepDto.logradouro());
        novoEndereco.setBairro(cepDto.bairro());
        novoEndereco.setLocalidade(cepDto.localidade());
        novoEndereco.setUf(cepDto.uf());
        novoEndereco.setNumero(request.numero());
        novoEndereco.setComplemento(request.complemento());

        Set<ConstraintViolation<CepDto>> errosAoValidarCep = validator.validate(cepDto);
        if (!CollectionUtils.isEmpty(errosAoValidarCep)) {
            throw new ConstraintViolationException("Erro ao validar CEP.", errosAoValidarCep);
        }

        colaborador.setEndereco(novoEndereco);
        colaboradorRepository.save(colaborador);
    }

    public void atualizarNomeColaborador(String cpf, String novoNome) {
        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }

        colaborador.setNome(novoNome);
        colaboradorRepository.save(colaborador);
    }

    public void inativarColaborador(String cpf) {
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }
        colaboradorRepository.inativarColaborador(cpf);
    }

    public List<ColaboradorResponse> listarColaboradoresPorEmpresa(String cnpj) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj);
        if (empresa == null) {
            throw new EmpresaNotFoundException(cnpj);
        }
        return colaboradorRepository.findAllByEmpresaAndAtivoTrue(empresa)
                .stream().map(ColaboradorResponse::new).toList();

    }
}
