package io.github.juliacanalle.creditointernoapi.service;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.ContaRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public Page<TransacaoResponse> listarTransacoesPorCpf(
            String cnpj,
            String cpf,
            LocalDate dataInicio,
            LocalDate dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Integer page,
            Integer size,
            String sort
    ) {
        Colaborador colaborador = colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)
                .orElseThrow(() -> new ColaboradorNotFoundException(cpf));

        Conta conta = colaborador.getConta();
        if (conta == null) {
            throw new ContaNotFoundException(cnpj, cpf);
        }

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

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        BigDecimal min = valorMin != null ? valorMin : BigDecimal.ZERO;
        BigDecimal max = valorMax != null ? valorMax : new BigDecimal("999999999");

        String[] sortParts = sort != null ? sort.split(",") : new String[]{"criadoEm", "desc"};
        String campo = sortParts[0].trim();
        String direcao = sortParts.length > 1 ? sortParts[1].trim() : "asc";

        if (!campo.equals("valor") && !campo.equals("criadoEm")) {
            throw new InvalidSortFieldException(campo);
        }

        Sort.Direction direction = direcao.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, campo));


        Page<Transacao> transacoes = transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(
                conta, inicio, fim, min, max, pageable);

        return transacoes.map(TransacaoResponse::new);
    }

    public List<Transacao> listarTodasTransacoesPorCpf(
            String cnpj,
            String cpf,
            LocalDate dataInicio,
            LocalDate dataFim,
            BigDecimal valorMin,
            BigDecimal valorMax,
            String sort
    ) {
        Colaborador colaborador = colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)
                .orElseThrow(() -> new ColaboradorNotFoundException(cpf));

        Conta conta = colaborador.getConta();
        if (conta == null) {
            throw new ContaNotFoundException(cnpj, cpf);
        }

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

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        BigDecimal min = valorMin != null ? valorMin : BigDecimal.ZERO;
        BigDecimal max = valorMax != null ? valorMax : new BigDecimal("999999999");

        String[] sortParts = sort != null ? sort.split(",") : new String[]{"criadoEm", "desc"};
        String campo = sortParts[0].trim();
        String direcao = sortParts.length > 1 ? sortParts[1].trim() : "asc";

        if (!campo.equals("valor") && !campo.equals("criadoEm")) {
            throw new InvalidSortFieldException(campo);
        }

        Sort.Direction direction = direcao.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sortObj = Sort.by(direction, campo);

        return transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(
                conta, inicio, fim, min, max, sortObj
        );
    }
}
