package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private Validator validator;

    @Mock
    private CepService cepService;

    @Test
    void creditarConta() {

        BigDecimal valor = new BigDecimal("1000");
        String cnpj = "12345678000199";
        String cpf = "1234567890";
        String mensagem = "VR Agosto/2025";

        Empresa empresa = new Empresa();
        empresa.setCnpj(cnpj);
        empresa.setAtivo(true);

        Conta conta = new Conta();

        Colaborador colab = new Colaborador();
        colab.setId(1);
        colab.setCpf(cpf);
        colab.setConta(conta);
        colab.setEmpresa(empresa);

        when(empresaRepository.findByCnpj(cnpj)).thenReturn(empresa);
        when(colaboradorRepository.findByCpf(cpf)).thenReturn(colab);
        when(validator.validate(any(Conta.class))).thenReturn(Collections.emptySet());

        contaService.creditarConta(valor, cnpj, cpf, mensagem);

        verify(empresaRepository).findByCnpj(cnpj);
        verify(colaboradorRepository).findByCpf(colab.getCpf());
        verify(validator).validate(any(Conta.class));
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void debitarConta() {

        BigDecimal valor = new BigDecimal("300");
        String cnpj = "12345678000199";
        String cpf = "1234567890";
        String mensagem = "Desconto Férias VR Agosto/2025";

        Empresa empresa = new Empresa();
        empresa.setCnpj(cnpj);
        empresa.setAtivo(true);

        Conta conta = new Conta();

        Colaborador colab = new Colaborador();
        colab.setId(1);
        colab.setCpf(cpf);
        colab.setConta(conta);
        colab.setEmpresa(empresa);

        when(empresaRepository.findByCnpj(cnpj)).thenReturn(empresa);
        when(colaboradorRepository.findByCpf(cpf)).thenReturn(colab);
        when(validator.validate(any(Conta.class))).thenReturn(Collections.emptySet());

        contaService.debitarConta(valor, cnpj, cpf, mensagem);

        verify(empresaRepository).findByCnpj(cnpj);
        verify(colaboradorRepository).findByCpf(colab.getCpf());
        verify(validator).validate(any(Conta.class));
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void deveLancarEmpresaNotFoundExceptionQuandoEmpresaNaoEncontradaAoCreditar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");

        when(empresaRepository.findByCnpj(empresa.getCnpj()))
                .thenReturn(null);

        assertThrows(EmpresaNotFoundException.class, () -> {
            contaService.creditarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarInactiveEmpresaExceptionQuandoEmpresaInativaAoCreditar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(false);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(InactiveEmpresaException.class, () -> {
            contaService.creditarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoColaboradorNaoEncontradoAoCreditar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(null);

        assertThrows(ColaboradorNotFoundException.class, () -> {
            contaService.creditarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarInactiveColaboradorExceptionQuandoColaboradorInativoAoCreditar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setAtivo(false);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(colaborador);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(InactiveColaboradorException.class, () -> {
            contaService.creditarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarColaboradorNotInCompanyExceptionQuandoColaboradorNaoPertenceEmpresaAoCreditar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        Empresa outra = new Empresa();
        outra.setCnpj("00000000000191");

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setEmpresa(outra);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(colaborador);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(ColaboradorNotInCompanyException.class, () -> {
            contaService.creditarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarEmpresaNotFoundExceptionQuandoEmpresaNaoEncontradaAoDebitar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");

        when(empresaRepository.findByCnpj(empresa.getCnpj()))
                .thenReturn(null);

        assertThrows(EmpresaNotFoundException.class, () -> {
            contaService.debitarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarInactiveEmpresaExceptionQuandoEmpresaInativaAoDebitar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(false);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(InactiveEmpresaException.class, () -> {
            contaService.debitarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoColaboradorNaoEncontradoAoDebitar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(null);

        assertThrows(ColaboradorNotFoundException.class, () -> {
            contaService.debitarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarInactiveColaboradorExceptionQuandoColaboradorInativoAoDebitar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setAtivo(false);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(colaborador);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(InactiveColaboradorException.class, () -> {
            contaService.debitarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }

    @Test
    void deveLancarColaboradorNotInCompanyExceptionQuandoColaboradorNaoPertenceEmpresaAoDebitar() {
        String cpf = "1234567890";
        BigDecimal valor = new BigDecimal("300");
        String mensagem = "Teste";

        Empresa empresa = new Empresa();
        empresa.setNome("Pluxee Brasil");
        empresa.setCnpj("12345678000199");
        empresa.setAtivo(true);

        Empresa outra = new Empresa();
        outra.setCnpj("00000000000191");

        Colaborador colaborador = new Colaborador();
        colaborador.setCpf(cpf);
        colaborador.setEmpresa(outra);

        when(colaboradorRepository.findByCpf(cpf))
                .thenReturn(colaborador);

        when(empresaRepository.findByCnpj("12345678000199"))
                .thenReturn(empresa);

        assertThrows(ColaboradorNotInCompanyException.class, () -> {
            contaService.debitarConta(valor, empresa.getCnpj(), cpf, mensagem);
        });
    }
}
