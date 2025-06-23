package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.exceptions.ColaboradorNotFoundException;
import io.github.juliacanalle.creditointernoapi.exceptions.CpfAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.EmpresaNotFoundException;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ColaboradorService {

    private final CepService cepService;
    private final EmpresaRepository empresaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ContaService contaService;

    public ColaboradorService(
            CepService cepService,
            EmpresaRepository empresaRepository,
            ColaboradorRepository colaboradorRepository,
            ContaService contaService)
    {
        this.cepService = cepService;
        this.empresaRepository = empresaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.contaService = contaService;
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

        return colaboradorRepository.save(colaborador);
    }

    public void atualizarEnderecoColaborador(@Valid ColaboradorRequest request, String cpf) {
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

        colaborador.setEndereco(novoEndereco);
    }
}
