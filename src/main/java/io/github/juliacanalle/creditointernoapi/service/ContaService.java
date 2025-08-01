package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.EnderecoRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.Set;

@Service
public class ContaService {

    private final EmpresaRepository empresaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final TransacaoRepository transacaoRepository;
    private final Validator validator;

    public ContaService(EmpresaRepository empresaRepository,
                        ColaboradorRepository colaboradorRepository,
                        TransacaoRepository transacaoRepository,
                        Validator validator) {
        this.empresaRepository = empresaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.transacaoRepository = transacaoRepository;
        this.validator = validator;
    }

    public void creditarConta(BigDecimal valor, String cnpj, String cpf, String mensagem) {
        var empresa = empresaRepository.findByCnpj(cnpj);
        if (empresa == null) {
            throw new EmpresaNotFoundException(cnpj);
        }

        if (!empresa.isAtivo()) {
            throw new InactiveEmpresaException(cnpj);
        }

        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }

        if (!colaborador.isAtivo()) {
            throw new InactiveColaboradorException(cpf);
        }

        var empresaDoColaborador = colaborador.getEmpresa();
        if (empresaDoColaborador != empresa) {
            throw new ColaboradorNotInCompanyException(cpf, cnpj);
        }

        Conta conta = colaborador.getConta();
        conta.creditar(valor);
        BigDecimal novoSaldo = conta.getSaldo();

        Set<ConstraintViolation<Conta>> errosAoValidarConta = validator.validate(conta);
        if (!CollectionUtils.isEmpty(errosAoValidarConta)) {
            throw new ConstraintViolationException("Existem erros nos campos da conta.", errosAoValidarConta);
        }

        Transacao t = new Transacao();
        t.setConta(conta);
        t.setValor(valor);
        t.setSaldo(novoSaldo);
        t.setTipoTransacao(TipoTransacao.CREDITO);
        t.setMensagem(mensagem);
        transacaoRepository.save(t);
    }

    public void debitarConta(BigDecimal valor, String cnpj, String cpf, String mensagem) {
        var empresa = empresaRepository.findByCnpj(cnpj);
        if (empresa == null) {
            throw new EmpresaNotFoundException(cnpj);
        }

        if (!empresa.isAtivo()) {
            throw new InactiveEmpresaException(cnpj);
        }

        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ColaboradorNotFoundException(cpf);
        }

        if (!colaborador.isAtivo()) {
            throw new InactiveColaboradorException(cpf);
        }

        var empresaDoColaborador = colaborador.getEmpresa();
        if (empresaDoColaborador != empresa) {
            throw new ColaboradorNotInCompanyException(cpf, cnpj);
        }
      
        Conta conta = colaborador.getConta();
        conta.debitar(valor);
        BigDecimal novoSaldo = conta.getSaldo();

        Set<ConstraintViolation<Conta>> errosAoValidarConta = validator.validate(conta);
        if (!CollectionUtils.isEmpty(errosAoValidarConta)) {
            throw new ConstraintViolationException("Existem erros nos campos da conta.", errosAoValidarConta);
        }

        Transacao t = new Transacao();
        t.setConta(conta);
        t.setValor(valor);
        t.setSaldo(novoSaldo);
        t.setTipoTransacao(TipoTransacao.DEBITO);
        t.setMensagem(mensagem);
        transacaoRepository.save(t);
    }
}

