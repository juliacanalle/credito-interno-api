package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.exceptions.CepNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class CepService {

    private final RestTemplate    restTemplate;
    private final Validator       validator;

    public CepService(RestTemplate restTemplate,
                      Validator validator) {
        this.restTemplate = restTemplate;
        this.validator    = validator;
    }

    public CepDto consultaCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json";

        ResponseEntity<CepDto> resp;
        try {
            resp = restTemplate.getForEntity(url, CepDto.class);
        } catch (RestClientException e) {
            throw new CepNotFoundException();
        }

        Set<ConstraintViolation<CepDto>> viol = validator.validate(resp.getBody());
        if (!viol.isEmpty()) {
            throw new ConstraintViolationException("Erro ao validar o CEP retornado.", viol);
        }

        return resp.getBody();
    }
}
