package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorResponse;
import io.github.juliacanalle.creditointernoapi.dto.OperacaoRequest;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.exceptions.ColaboradorNotFoundException;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import io.github.juliacanalle.creditointernoapi.service.CepService;
import io.github.juliacanalle.creditointernoapi.service.ColaboradorService;
import io.github.juliacanalle.creditointernoapi.service.ContaService;
import io.github.juliacanalle.creditointernoapi.service.TransacaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/empresas/{cnpj}/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ColaboradorService colaboradorService;

    @Autowired
    private CepService cepService;

    @Autowired
    private ContaService contaService;

    private Colaborador colaborador;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TransacaoService transacaoService;

    @Transactional
    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cnpj") String cnpj) {
        Colaborador colaboradorSalvo = colaboradorService.cadastrarColaboradorComBuscaCep(request, cnpj);
        ColaboradorResponse response = new ColaboradorResponse(colaboradorSalvo);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Colaborador> listarColaboradores(@PathVariable("cnpj") String cnpj) {
        return colaboradorRepository.findAllByAtivoTrue();
    }

    @GetMapping("/{cpf}")
    public Colaborador buscarColaboradorPorCpf(@PathVariable("cpf") String cpf) {
        return colaboradorRepository.findByCpf(cpf);
    }

    @DeleteMapping("/{cpf}")
    public void inativar(@PathVariable("cpf") String cpf) {
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }
        colaboradorRepository.delete(colaborador);
    }

    @PatchMapping("/{cpf}")
    public void atualizarNome(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cpf") String cpf) {
        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }
        colaborador.atualizarNome(request.nome());
    }

    @PatchMapping("/{cpf}")
    public void atualizarEndereco(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cpf") String cpf) {
        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }
        colaboradorService.atualizarEnderecoColaborador(request, cpf);
    }

    @PostMapping("/{cpf}/creditar")
    public void creditarConta(@RequestBody @Valid OperacaoRequest request, @PathVariable("cpf") String cpf, @PathVariable("cnpj") String cnpj) {
        contaService.creditarConta(request.valor(), cnpj, cpf, request.mensagem());
    }

    @Transactional
    @PostMapping("/{cpf}/debitar")
    public void debitarConta(@RequestBody @Valid OperacaoRequest request, @PathVariable("cpf") String cpf, @PathVariable("cnpj") String cnpj) {
        contaService.debitarConta(request.valor(), cnpj, cpf, request.mensagem());
    }

    @GetMapping("/{cpf}/transacoes")
    public List<TransacaoResponse> extratoColaboradorPorCpf(@PathVariable("cpf") String cpf, @PathVariable("cnpj") String cnpj) {
        return transacaoService.listarTransacoesPorCpf(cnpj, cpf);
    }
}
