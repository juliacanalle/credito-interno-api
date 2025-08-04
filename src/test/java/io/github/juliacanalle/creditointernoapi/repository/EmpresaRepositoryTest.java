package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    EmpresaRepository empresaRepository;

    private Endereco enderecoPadrao() {
        Endereco endereco = new Endereco();
        endereco.setCep("00000000");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setLocalidade("Guarulhos");
        endereco.setUf("SP");
        endereco.setComplemento("Casa");
        return endereco;
    }

    @Test
    void findByCnpjSuccess() {
        Empresa empresa = new Empresa("Empresa X", "29876543000110", enderecoPadrao());
        entityManager.persist(empresa);

        Empresa empresaEncontrada = empresaRepository.findByCnpj(empresa.getCnpj());

        assertEquals("29876543000110", empresaEncontrada.getCnpj());
    }

    @Test
    void updateCompanyNameSuccess() {
        Empresa empresa = new Empresa("Empresa X", "29876543000110", enderecoPadrao());
        entityManager.persist(empresa);

        empresaRepository.atualizarNome(empresa.getCnpj(), "Empresa XY");

        entityManager.flush();
        entityManager.clear();

        Empresa empresaNomeAlterado = empresaRepository.findByCnpj(empresa.getCnpj());
        assertEquals("Empresa XY", empresaNomeAlterado.getNome());
    }

    @Test
    void updateCompanyCnpjSuccess() {
        Empresa empresa = new Empresa("Empresa X", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        empresaRepository.atualizarCnpj(empresa.getCnpj(), "12345678000120");

        entityManager.flush();
        entityManager.clear();

        Empresa empresaCnpjAlterado = empresaRepository.findByCnpj("12345678000120");
        assertEquals("12345678000120", empresaCnpjAlterado.getCnpj());
    }

    @Test
    void inactiveCompanySuccess() {
        Empresa empresa = new Empresa("Empresa X", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        empresaRepository.inativarEmpresa(empresa.getCnpj());
        entityManager.flush();
        entityManager.clear();

        Empresa empresaInativada = empresaRepository.findByCnpj(empresa.getCnpj());
        assertEquals("12345678000199", empresaInativada.getCnpj());
        assertFalse(empresaInativada.isAtivo());
    }

    @Test
    void existsByCnpjSuccess() {
        Empresa empresa = new Empresa("Empresa X", "12345678000120", enderecoPadrao());
        entityManager.persist(empresa);

        empresaRepository.existsByCnpj(empresa.getCnpj());

        Empresa empresaEncontrada = empresaRepository.findByCnpj(empresa.getCnpj());
        assertEquals("12345678000120", empresaEncontrada.getCnpj());
    }

    @Test
    void getCompanyCountSuccess() {
        Empresa empresa1 = new Empresa("Empresa X", "12345678000120", enderecoPadrao());
        entityManager.persist(empresa1);

        Empresa empresa2 = new Empresa("Empresa XY", "12345678000118", enderecoPadrao());
        entityManager.persist(empresa2);

        Empresa empresa3 = new Empresa("Empresa XYZ", "12345678000117", enderecoPadrao());
        entityManager.persist(empresa3);

        long contagemDeEmpresas = empresaRepository.contagemDeEmpresas();
        assertEquals(3, contagemDeEmpresas);
        assertEquals("12345678000120", empresa1.getCnpj());
    }
}
