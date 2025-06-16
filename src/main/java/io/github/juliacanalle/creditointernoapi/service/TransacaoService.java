package io.github.juliacanalle.creditointernoapi.service;
import io.github.juliacanalle.creditointernoapi.dto.TransacaoResponse;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public List <TransacaoResponse> listarTransacoesPorCpf (String cnpj, String cpf) {
        var empresa = empresaRepository.findByCnpj(cnpj);
        if (empresa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!empresa.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var colaborador = colaboradorRepository.findByCpf(cpf);
        if (colaborador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!colaborador.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var empresaDoColaborador = colaborador.getEmpresa();
        if (empresaDoColaborador != empresa) {
            throw new IllegalArgumentException("Esse colaborador não pertence a empresa " + empresa + ".");
        }

        Conta conta = colaborador.getConta();
        List<Transacao> transacoes = transacaoRepository.findByConta(conta);

        List<TransacaoResponse> respostas = transacoes.stream()
                .map(TransacaoResponse::new)
                .collect(Collectors.toList());
        return respostas;
    }



}
