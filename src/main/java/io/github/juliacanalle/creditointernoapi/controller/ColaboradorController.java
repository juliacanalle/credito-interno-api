package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorResponse;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import io.github.juliacanalle.creditointernoapi.service.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas/{cnpj}/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
   private ColaboradorService colaboradorService;

    @Transactional
    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@RequestBody @Valid ColaboradorRequest request, @PathVariable("cnpj") String cnpj) {
        Colaborador colaboradorSalvo = colaboradorService.cadastrarColaboradorComBuscaCep(request, cnpj);
        ColaboradorResponse response = new ColaboradorResponse(colaboradorSalvo);

        return ResponseEntity.ok(response);
    }




}
