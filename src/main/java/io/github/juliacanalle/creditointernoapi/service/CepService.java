package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.exceptions.CepNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    public CepDto consultaCep (String cep) {
        try {
            String url = "https://viacep.com.br/ws/".concat(cep).concat("/json");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CepDto> cepDtoResponseEntity =
                    restTemplate.getForEntity(url,CepDto.class);
            return cepDtoResponseEntity.getBody();
        } catch (Exception e) {
            throw new CepNotFoundException();
        }
    }
}
