package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.exceptions.*;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.EnderecoRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class ContaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

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

        Transacao t = new Transacao();
        t.setConta(conta);
        t.setValor(valor);
        t.setSaldo(novoSaldo);
        t.setTipoTransacao(TipoTransacao.DEBITO);
        t.setMensagem(mensagem);
        transacaoRepository.save(t);
    }
}

