package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.DadosAtualizaCadastroEmpresa;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaResponse;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private EmpresaService empresaService;


    @PostMapping
    public ResponseEntity<EmpresaResponse> cadastrar(@RequestBody @Valid EmpresaRequest request) {
        Empresa empresaSalva = empresaService.cadastrarEmpresaComBuscaCep(request);
        EmpresaResponse response = new EmpresaResponse(empresaSalva);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    @PutMapping("/{cnpjAtual}")
    public void atualizar( @PathVariable String cnpjAtual, @RequestBody @Valid DadosAtualizaCadastroEmpresa dados) {
        empresaService.atualizarEmpresa(cnpjAtual, dados);
    }

    @DeleteMapping("/{cnpjAtual}")
    public void deletar(@PathVariable String cnpjAtual) {
        empresaService.inativarEmpresa(cnpjAtual);
    }
}
