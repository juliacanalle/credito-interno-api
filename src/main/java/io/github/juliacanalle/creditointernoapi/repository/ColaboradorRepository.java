package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    public Colaborador findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Colaborador> findAllByAtivoTrue();
}
