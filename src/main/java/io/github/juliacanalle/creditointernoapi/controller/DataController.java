package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dados")
public class DataController {

    @Autowired
    ColaboradorRepository colaboradorRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

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
}
