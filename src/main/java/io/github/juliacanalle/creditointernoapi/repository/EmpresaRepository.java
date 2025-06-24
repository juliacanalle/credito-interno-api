package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByCnpj(String cnpj);

    @Query("UPDATE Empresa e SET e.cnpj = :cnpjNovo, e.nome = :nomeNovo WHERE e.cnpj = :cnpjAtual")
    @Transactional
    @Modifying
    void atualizarEmpresa(@Param("cnpjAtual") String cnpjAtual,
                                 @Param("cnpjNovo") String cnpjNovo,
                                 @Param("nomeNovo") String nomeNovo);

    @Query("UPDATE Empresa e SET e.ativo = false WHERE e.cnpj = :cnpjAtual")
    @Transactional
    @Modifying
    void inativarEmpresa(@Param("cnpjAtual") String cnpjAtual);
}

