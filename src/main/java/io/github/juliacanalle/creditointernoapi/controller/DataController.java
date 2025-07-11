package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/dados")
public class DataController {

    @Autowired
    ColaboradorRepository colaboradorRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private TransacaoRepository transacaoRepository;

    //Exibe a qtd de colaboradores de todas as empresas, ou de uma empresa específica
    @GetMapping("/colaboradores")
    public long contagemDeColaboradores (@RequestParam(required = false) String cnpj) {
        if (cnpj == null) {
            return colaboradorRepository.count();
        }
        return colaboradorRepository.contagemDeColaboradoresAtivosPorCnpj(cnpj);
    }

    //Exibe a qtd total de empresas cadastradas no sistema
    @GetMapping("/empresas")
    public long contagemDeEmpresas () {
        return empresaRepository.contagemDeEmpresas();
    }

    //Exibe a qtd de créditos liberados de todas as empresas, ou de uma empresa específica
    @GetMapping("/creditos")
    public BigDecimal contagemCreditosLiberados(
            @RequestParam TipoTransacao tipoTransacao,
            @RequestParam(required = false) String cnpj
    ) {
        return transacaoRepository.somaDeCreditosLiberados(tipoTransacao, cnpj);
    }
}
