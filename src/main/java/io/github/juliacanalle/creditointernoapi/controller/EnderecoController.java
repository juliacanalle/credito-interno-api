package io.github.juliacanalle.creditointernoapi.controller;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class EnderecoController {

    private CepService service;

    @Autowired
    public void setCepService(CepService service) {
        this.service = service;
    }

    @GetMapping(value = "/{cep}")
    public ResponseEntity<CepDto> consultaCep(@PathVariable String cep) {
return ResponseEntity.ok(service.consultaCep(cep));
    }
}
