package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    public Empresa findByCnpj(String cnpj);

    @Query("UPDATE Empresa e SET e.cnpj = :cnpjNovo, e.nome = :nomeNovo WHERE e.cnpj = :cnpjAtual")
    @Transactional
    @Modifying
    public void atualizarEmpresa(@Param("cnpjAtual") String cnpjAtual,
                                 @Param("cnpjNovo") String cnpjNovo,
                                 @Param("nomeNovo") String nomeNovo);

    @Query("UPDATE Empresa e SET e.ativo = false WHERE e.cnpj = :cnpjAtual")
    @Transactional
    @Modifying
    public void inativarEmpresa(@Param("cnpjAtual") String cnpjAtual);
}

