package io.github.juliacanalle.creditointernoapi.service;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.exceptions.ColaboradorNotFoundException;
import io.github.juliacanalle.creditointernoapi.exceptions.ContaNotFoundException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            Pageable pageable
    ) {
        Colaborador colaborador = colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)
                .orElseThrow(() -> new ColaboradorNotFoundException(cpf));

        Conta conta = colaborador.getConta();
        if (conta == null) {
            throw new ContaNotFoundException(cnpj, cpf);
        }

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        BigDecimal min = valorMin != null ? valorMin : BigDecimal.ZERO;
        BigDecimal max = valorMax != null ? valorMax : new BigDecimal("999999999");

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
            Sort sort
    ) {
        Colaborador colaborador = colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)
                .orElseThrow(() -> new ColaboradorNotFoundException(cpf));

        Conta conta = colaborador.getConta();
        if (conta == null) {
            throw new ContaNotFoundException(cnpj, cpf);
        }

        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        BigDecimal min = valorMin != null ? valorMin : BigDecimal.ZERO;
        BigDecimal max = valorMax != null ? valorMax : new BigDecimal("999999999");

        return transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(
                conta, inicio, fim, min, max, sort
        );
    }
}
