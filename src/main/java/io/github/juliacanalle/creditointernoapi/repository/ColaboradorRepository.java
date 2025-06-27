package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    public Colaborador findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Colaborador> findAllByEmpresaAndAtivoTrue(Empresa empresa);

    @Query("""
UPDATE Colaborador c SET c.ativo = false WHERE c.cpf = :cpf
""")
    @Transactional
    @Modifying
    public void inativarColaborador(@Param("cpf") String cpf);

    @Query("""
    SELECT c.conta FROM Colaborador c 
    WHERE c.cpf = :cpf AND c.empresa.cnpj = :cnpj
""")

    Optional<Conta> findByCpfAndCnpj(@Param("cpf") String cpf, @Param("cnpj") String cnpj);

    Optional<Colaborador> findByCpfAndEmpresaCnpj(String cpf, String cnpj);

    String cpf(String cpf);
}
