package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Colaborador;

public interface ColaboradorRepository {

    public Colaborador findByCpf(String cpf);

}
