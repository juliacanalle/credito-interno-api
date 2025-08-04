package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Test
    void listarTransacoesPorCpf () {

        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        int page = 0;
        int size = 10;
        String sort = "criadoEm,desc";

        LocalDateTime dataInicio = LocalDateTime.of(2025, 8, 31, 10, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 8, 31, 10, 30);

        LocalDate dataInicioLocalDate = dataInicio.toLocalDate();
        LocalDate dataFimLocalDate = dataFim.toLocalDate();

        Empresa empresa = new Empresa();
        empresa.setCnpj(cnpj);

        Conta conta = new Conta();

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setConta(conta);
        colaborador.setEmpresa(empresa);

        conta.setColaborador(colaborador);

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)).thenReturn(Optional.of(colaborador));

        Transacao t1 = new Transacao();
        t1.setConta(conta);
        t1.setValor(new BigDecimal("1500"));
        t1.setSaldo(new BigDecimal("100"));
        t1.setCriadoEm(dataInicio);
        t1.setTipoTransacao(TipoTransacao.CREDITO);
        t1.setMensagem("VR Setembro/2025");

        Transacao t2 = new Transacao();
        t2.setConta(conta);
        t2.setValor(new BigDecimal("1300"));
        t2.setSaldo(new BigDecimal("100"));
        t2.setCriadoEm(dataInicio);
        t2.setTipoTransacao(TipoTransacao.CREDITO);
        t2.setMensagem("VA Setembro/2025");

        Transacao t3 = new Transacao();
        t3.setConta(conta);
        t3.setValor(new BigDecimal("50"));
        t3.setSaldo(new BigDecimal("100"));
        t3.setCriadoEm(dataInicio);
        t3.setTipoTransacao(TipoTransacao.CREDITO);
        t3.setMensagem("Vale Cultura Setembro/2025");

        Page<Transacao> pageMock = new PageImpl<>(List.of(t1, t2));

        when(transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(
                ArgumentMatchers.eq(conta),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                ArgumentMatchers.eq(valorMin),
                ArgumentMatchers.eq(valorMax),
                any(Pageable.class)
        )).thenReturn(pageMock);

        Page<TransacaoResponse> resultado = transacaoService.listarTransacoesPorCpf(
                cnpj, cpf, dataInicioLocalDate, dataFimLocalDate, valorMin, valorMax, page, size, sort
        );

        assertEquals(2, resultado.getTotalElements());
        assertEquals(t1.getValor(), resultado.getContent().get(0).valor());
        assertEquals(t2.getValor(), resultado.getContent().get(1).valor());
    }

    @Test
    void listarTodasTransacoesPorCpf() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        String sort = "criadoEm,desc";

        LocalDateTime dataInicio = LocalDateTime.of(2025, 8, 31, 10, 30);
        LocalDateTime dataFim = LocalDateTime.of(2025, 8, 31, 10, 30);

        LocalDate dataInicioLocalDate = dataInicio.toLocalDate();
        LocalDate dataFimLocalDate = dataFim.toLocalDate();

        Empresa empresa = new Empresa();
        empresa.setCnpj(cnpj);

        Conta conta = new Conta();

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setConta(conta);
        colaborador.setEmpresa(empresa);

        conta.setColaborador(colaborador);

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)).thenReturn(Optional.of(colaborador));

        Transacao t1 = new Transacao();
        t1.setConta(conta);
        t1.setValor(new BigDecimal("1500"));
        t1.setSaldo(new BigDecimal("100"));
        t1.setCriadoEm(dataInicio);
        t1.setTipoTransacao(TipoTransacao.CREDITO);
        t1.setMensagem("VR Setembro/2025");

        Transacao t2 = new Transacao();
        t2.setConta(conta);
        t2.setValor(new BigDecimal("1300"));
        t2.setSaldo(new BigDecimal("100"));
        t2.setCriadoEm(dataInicio);
        t2.setTipoTransacao(TipoTransacao.CREDITO);
        t2.setMensagem("VA Setembro/2025");

        when(transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(
                ArgumentMatchers.eq(conta),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                ArgumentMatchers.eq(valorMin),
                ArgumentMatchers.eq(valorMax),
                any(Sort.class)
        )).thenReturn(List.of(t1, t2));

        List<Transacao> resultado = transacaoService.listarTodasTransacoesPorCpf(
                cnpj, cpf, dataInicioLocalDate, dataFimLocalDate, valorMin, valorMax, sort
        );

        assertEquals(2, resultado.size());
        assertEquals("VR Setembro/2025", resultado.get(0).getMensagem());
        assertEquals("VA Setembro/2025", resultado.get(1).getMensagem());
        assertEquals(new BigDecimal("1500"), resultado.get(0).getValor());
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoColaboradorNaoEncontradoCenarioPageable() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        int page = 0;
        int size = 10;
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)).thenReturn(Optional.empty());

        assertThrows(ColaboradorNotFoundException.class, () -> {
            transacaoService.listarTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    page, size, sort
            );
        });
    }

    @Test
    void deveLancarContaNotFoundExceptionQuandoContaNaoEncontradoPageable() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        int page = 0;
        int size = 10;
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(null);

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(ContaNotFoundException.class, () -> {
            transacaoService.listarTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    page, size, sort
            );
        });
    }

    @Test
    void deveLancarDataRangeExceedLimitExceptionQuandoPassarDe30DiasPageable() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        int page = 0;
        int size = 10;
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(31);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(DataRangeExceedLimitException.class, () -> {
            transacaoService.listarTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    page, size, sort
            );
        });
    }

    @Test
    void deveLancarMinValueGreaterThanMaxValueExceptionQuandoValorMinimoForMaiorQueValorMaximoPageable() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("2000");
        BigDecimal valorMax = new BigDecimal("1000");
        int page = 0;
        int size = 10;
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(MinValueGreaterThanMaxValueException.class, () -> {
            transacaoService.listarTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    page, size, sort
            );
        });
    }

    @Test
    void deveLancarInvalidSortFieldExceptionQuandoCampoDeOrdenacaoEhInvalidoPageable() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        int page = 0;
        int size = 10;
        String sort = "invalido,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(InvalidSortFieldException.class, () -> {
            transacaoService.listarTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    page, size, sort
            );
        });
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoColaboradorNaoEncontradoCenarioPageableList() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj)).thenReturn(Optional.empty());

        assertThrows(ColaboradorNotFoundException.class, () -> {
            transacaoService.listarTodasTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    sort
            );
        });
    }

    @Test
    void deveLancarContaNotFoundExceptionQuandoContaNaoEncontradoPageableList() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(null);

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(ContaNotFoundException.class, () -> {
            transacaoService.listarTodasTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    sort
            );
        });
    }

    @Test
    void deveLancarDataRangeExceedLimitExceptionQuandoPassarDe30DiasPageableList() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(31);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(DataRangeExceedLimitException.class, () -> {
            transacaoService.listarTodasTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    sort
            );
        });
    }

    @Test
    void deveLancarMinValueGreaterThanMaxValueExceptionQuandoValorMinimoForMaiorQueValorMaximoPageableList() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("2000");
        BigDecimal valorMax = new BigDecimal("1000");
        String sort = "criadoEm,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(MinValueGreaterThanMaxValueException.class, () -> {
            transacaoService.listarTodasTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    sort
            );
        });
    }

    @Test
    void deveLancarInvalidSortFieldExceptionQuandoCampoDeOrdenacaoEhInvalidoList() {
        String cpf = "00000000000";
        String cnpj = "00000000000000";
        BigDecimal valorMin = new BigDecimal("1000");
        BigDecimal valorMax = new BigDecimal("2000");
        String sort = "invalido,desc";

        LocalDate dataInicio = LocalDate.now().minusDays(5);
        LocalDate dataFim = LocalDate.now();

        Colaborador colaborador = Mockito.mock(Colaborador.class);
        when(colaborador.getConta()).thenReturn(new Conta());

        when(colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj))
                .thenReturn(Optional.of(colaborador));

        assertThrows(InvalidSortFieldException.class, () -> {
            transacaoService.listarTodasTransacoesPorCpf(
                    cnpj, cpf,
                    dataInicio, dataFim,
                    valorMin, valorMax,
                    sort
            );
        });
    }

}

