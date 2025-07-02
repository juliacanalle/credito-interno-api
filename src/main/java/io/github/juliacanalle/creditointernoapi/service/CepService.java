package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.exceptions.CepNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class CepService {

    public final Validator validator;

    public CepService(Validator validator) {
        this.validator = validator;
    }

    public CepDto consultaCep (String cep) {
        try {
            String url = "https://viacep.com.br/ws/".concat(cep).concat("/json");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CepDto> cepDtoResponseEntity =
                    restTemplate.getForEntity(url,CepDto.class);

            Set<ConstraintViolation<CepDto>> violacoes = validator.validate(cepDtoResponseEntity.getBody());
            if (!violacoes.isEmpty()) {
                throw new ConstraintViolationException("Erro ao validar o CEP retornado.", violacoes);
            }

            return cepDtoResponseEntity.getBody();
        } catch (Exception e) {
            throw new CepNotFoundException();
        }
    }
}
