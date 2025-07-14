package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.*;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    private ContaService contaService;

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private CsvExportacaoService csvExporterService;

    @Transactional
    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cnpj") String cnpj) {
        Colaborador colaboradorSalvo = colaboradorService.cadastrarColaboradorComBuscaCep(request, cnpj);
        ColaboradorResponse response = new ColaboradorResponse(colaboradorSalvo);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ColaboradorResponse> listarPorEmpresa(@PathVariable String cnpj) {
        return colaboradorService.listarColaboradoresPorEmpresa(cnpj);
    }

    @GetMapping("/{cpf}")
    public ColaboradorResponse buscarColaboradorPorCpf(@PathVariable("cpf") String cpf) {
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);
        return new ColaboradorResponse(colaborador);
    }

    @DeleteMapping("/{cpf}")
    public void inativar(@PathVariable("cpf") String cpf) {
        colaboradorService.inativarColaborador(cpf);
    }

    @PatchMapping("/{cpf}/nome")
    public void atualizarNome(@RequestBody @Valid AtualizaNomeRequest request, @PathVariable("cpf") String cpf, String novoNome) {
        colaboradorService.atualizarNomeColaborador(cpf, request.nome());
    }

    @PatchMapping("/{cpf}/endereco")
    public void atualizarEndereco(@RequestBody @Valid AtualizaEnderecoRequest request, @PathVariable("cpf") String cpf) {
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
    public Page<TransacaoResponse> extratoColaboradorPorCpf(
            @PathVariable String cnpj,
            @PathVariable String cpf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "criadoEm,desc") String sort
    ) {
        return transacaoService.listarTransacoesPorCpf(
                cnpj, cpf, dataInicio, dataFim, valorMin, valorMax, page, size, sort
        );
    }

    @GetMapping(value = "/{cpf}/transacoes/exportar", produces = "text/csv")
    public void exportarExtratoCsv(
            @PathVariable String cnpj,
            @PathVariable String cpf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(defaultValue = "criadoEm,desc") String sort,
            HttpServletResponse response
    ) throws IOException {

        List<Transacao> transacoes = transacaoService.listarTodasTransacoesPorCpf(
                cnpj, cpf, dataInicio, dataFim, valorMin, valorMax, sort
        );

        csvExporterService.exportarTransacoes(transacoes, response, cpf);
    }

}
