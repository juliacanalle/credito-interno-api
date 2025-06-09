package io.github.juliacanalle.creditointernoapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record CepDto(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        Long ibge,
        Long gia,
        Integer ddd,
        Long siafi) {
}
