package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.exceptions.CepNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CepServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Validator validator;

    @InjectMocks
    private CepService cepService;

    @Test
    void deveRetornarCepDtoQuandoCepValido() {
        String cep = "01001000";
        String url = "https://viacep.com.br/ws/" + cep + "/json";

        CepDto dto = new CepDto(
                "01001000",
                "Praça da Sé",
                "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                3550308L,
                1004L,
                11,
                7107L
        );
        ResponseEntity<CepDto> response = new ResponseEntity<>(dto, HttpStatus.OK);

        when(restTemplate.getForEntity(url, CepDto.class)).thenReturn(response);
        when(validator.validate(dto)).thenReturn(Collections.emptySet());

        CepDto resultado = cepService.consultaCep(cep);

        assertEquals("Praça da Sé", resultado.logradouro());
        verify(restTemplate).getForEntity(url, CepDto.class);
        verify(validator).validate(dto);
    }

    @Test
    void deveLancarCepNotFoundExceptionQuandoRestTemplateFalhar() {
        String cep = "99999999";
        String url = "https://viacep.com.br/ws/" + cep + "/json";

        when(restTemplate.getForEntity(url, CepDto.class))
                .thenThrow(new RestClientException("erro de HTTP"));

        assertThrows(CepNotFoundException.class, () -> cepService.consultaCep(cep));
    }

    @Test
    void deveLancarConstraintViolationExceptionQuandoDtoInvalido() {
        String cep = "01001000";
        String url = "https://viacep.com.br/ws/" + cep + "/json";

        CepDto dtoInvalido = new CepDto("01001000","","","","","",null,null,null,null);
        ResponseEntity<CepDto> response = new ResponseEntity<>(dtoInvalido, HttpStatus.OK);

        ConstraintViolation<CepDto> viol = mock(ConstraintViolation.class);

        when(restTemplate.getForEntity(url, CepDto.class)).thenReturn(response);
        when(validator.validate(dtoInvalido)).thenReturn(Set.of(viol));

        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> cepService.consultaCep(cep)
        );
        assertTrue(ex.getMessage().contains("Erro ao validar o CEP retornado."));
    }
}
