package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    public Colaborador findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Colaborador> findAllByAtivoTrue();

    @Query("UPDATE Colaborador c SET c.ativo = false WHERE c.cpf = :cpf")
    @Transactional
    @Modifying
    public void inativarColaborador(@Param("cpf") String cpf);
}
