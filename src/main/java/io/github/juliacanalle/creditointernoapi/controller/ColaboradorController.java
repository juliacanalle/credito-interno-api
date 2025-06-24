package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorResponse;
import io.github.juliacanalle.creditointernoapi.dto.OperacaoRequest;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import io.github.juliacanalle.creditointernoapi.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
        Empresa empresa = empresaRepository.findByCnpj(cnpj);
                if(empresa == null) {
                    throw new EmpresaNotFoundException(cnpj);
                }

        return colaboradorRepository.findAllByEmpresaAndAtivoTrue(empresa)
                .stream().map(ColaboradorResponse::new).toList();
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
        colaboradorRepository.inativarColaborador(cpf);
    }

    @PatchMapping("/{cpf}/nome")
    public void atualizarNome(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cpf") String cpf) {
        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }
        colaborador.atualizarNome(request.nome());
    }

    @PatchMapping("/{cpf}/endereco")
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
    public Page<TransacaoResponse> extratoColaboradorPorCpf(
            @PathVariable String cnpj,
            @PathVariable String cpf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dataHora,desc") String sort) {
        if (dataInicio == null || dataFim == null) {
            dataFim = LocalDate.now();
            dataInicio = dataFim.minusDays(30);
        }

        if (ChronoUnit.DAYS.between(dataInicio, dataFim) > 30) {
            throw new DataRangeExceedLimitException(dataInicio, dataFim);
        }

        if (valorMin != null && valorMax != null && valorMin.compareTo(valorMax) > 0) {
            throw new MinValueGreaterThanMaxValueException(valorMin, valorMax);
        }

        String[] sortParts = sort.split(",");
        String campo = sortParts[0];
        String direcao = sortParts.length > 1 ? sortParts[1] : "asc";

        if (!campo.equals("valor") && !campo.equals("criadoEm")) {
            throw new InvalidSortFieldException(campo);
        }

        Sort.Direction direction = direcao.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, campo));

        return transacaoService.listarTransacoesPorCpf(
                cnpj, cpf, dataInicio, dataFim, valorMin, valorMax, pageable
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

        if (dataInicio == null || dataFim == null) {
            dataFim = LocalDate.now();
            dataInicio = dataFim.minusDays(30);
        }

        String[] sortParts = sort.split(",");
        String campo = sortParts[0];
        String direcao = sortParts.length > 1 ? sortParts[1] : "asc";

        Sort.Direction direction = direcao.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sortObj = Sort.by(direction, campo);

        List<Transacao> transacoes = transacaoService.listarTodasTransacoesPorCpf(
                cnpj, cpf, dataInicio, dataFim, valorMin, valorMax, sortObj
        );

        csvExporterService.exportarTransacoes(transacoes, response, cpf);
    }
}
