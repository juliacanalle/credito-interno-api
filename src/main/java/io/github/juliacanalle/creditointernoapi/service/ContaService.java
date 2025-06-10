package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
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

    public void creditarConta(BigDecimal valor, String cnpj, String cpf) {
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
         colaborador.getConta().creditar(valor);
    }
}
