package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Conta;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.model.Endereco;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class ColaboradorRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ColaboradorRepository colaboradorRepository;

    private Endereco enderecoPadrao() {
        Endereco endereco = new Endereco();
        endereco.setCep("07134500");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("151");
        endereco.setBairro("Centro");
        endereco.setLocalidade("Guarulhos");
        endereco.setUf("SP");
        endereco.setComplemento("Casa");
        return endereco;
    }

    private void criarColaborador(ColaboradorRequest request, Empresa empresa, Conta conta) {

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(request.nome());
        colaborador.setCpf(request.cpf());
        colaborador.setEndereco(enderecoPadrao());
        colaborador.setEmpresa(empresa);
        colaborador.setConta(conta);

        entityManager.persist(colaborador);

        this.entityManager.persist(colaborador);
    }

    @Test
    @DisplayName("Should get employee successfully")
    void findByCpfSuccess() {

        Conta conta = new Conta();

        String cpf = "00000000000";
        ColaboradorRequest request = new ColaboradorRequest("Julia Teste", cpf, "00000000", "100", "Casa" );

        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        this.criarColaborador(request, empresa, conta);
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);

        assertNotNull(colaborador, "Colaborador não deveria ser nulo");
        assertEquals(cpf, colaborador.getCpf());
        assertEquals("Julia Teste", colaborador.getNome());
    }

    @Test
    @DisplayName("Should not get employee")
    void findByCpfFailed() {
        String cpf = "99999999999";
        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);

        assertNull(colaborador, "Colaborador não encontrado. ");
    }

    @Test
    @DisplayName("Should return true when CPF exists")
    void existsByCpfSuccess() {
        Conta conta = new Conta();

        String cpf = "00000000000";
        ColaboradorRequest request = new ColaboradorRequest("Julia Teste", cpf, "00000000", "100", "Casa" );
        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        criarColaborador(request, empresa, conta);

        boolean exists = colaboradorRepository.existsByCpf(cpf);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when CPF not exists")
    void existsByCpfFailed() {
        String cpf = "99999999999";
        boolean exists = colaboradorRepository.existsByCpf(cpf);

        assertFalse(exists);
    }

    @Test
    void findAllByEmpresaAndAtivoTrueSuccess() {

        Conta conta = new Conta();

        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        ColaboradorRequest colaboradorAtivo1 = new ColaboradorRequest("Julia Teste", "54239214977", "00000000", "100", "Casa");
        this.criarColaborador(colaboradorAtivo1, empresa, conta);

        ColaboradorRequest colaboradorAtivo2 = new ColaboradorRequest("Julia Teste 2", "54229215976", "00000000", "100", "Casa");
        this.criarColaborador(colaboradorAtivo2, empresa, conta);

        List<Colaborador> resultado = colaboradorRepository.findAllByEmpresaAndAtivoTrue(empresa);

        assertEquals(2, resultado.size());
        assertEquals("Julia Teste", resultado.get(0).getNome());
        assertTrue(resultado.get(0).isAtivo());
        assertTrue(resultado.get(1).isAtivo());
        assertEquals("12345678000199", empresa.getCnpj());
    }

    @Test
    void inactivateCollaboratorSuccess() {

        Conta conta = new Conta();

        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        String cpf = "54239214977";

        ColaboradorRequest request = new ColaboradorRequest("Julia Teste", cpf, "00000000", "100", "Casa");
        this.criarColaborador(request, empresa, conta);

        colaboradorRepository.inativarColaborador(request.cpf());
        entityManager.flush();
        entityManager.clear();

        Colaborador colaborador = colaboradorRepository.findByCpf(cpf);

        assertNotNull(colaborador);
        assertFalse(colaborador.isAtivo());
    }

    @Test
    void findByCpfAndEmpresaCnpjSuccess() {

        Conta conta = new Conta();

        String cnpj = "12345678000199";

        Empresa empresa = new Empresa("Pluxee", cnpj, enderecoPadrao());
        entityManager.persist(empresa);

        String cpf = "54239214977";

        ColaboradorRequest request = new ColaboradorRequest("Julia Teste", cpf, "00000000", "100", "Casa");
        this.criarColaborador(request, empresa, conta);

        Optional<Colaborador> resultado = colaboradorRepository.findByCpfAndEmpresaCnpj(cpf, cnpj);

        Colaborador colaborador = resultado.orElseThrow(() -> new AssertionError("Colaborador não encontrado"));

        assertEquals("54239214977", colaborador.getCpf());
        assertEquals("Julia Teste", colaborador.getNome());
        assertEquals("12345678000199", colaborador.getEmpresa().getCnpj());
    }

    @Test
    void countActiveEmployeesByCnpjSuccess() {

        Conta conta = new Conta();

        String cnpj = "12345678000199";

        Empresa empresa = new Empresa("Pluxee", cnpj, enderecoPadrao());
        entityManager.persist(empresa);

        ColaboradorRequest colaborador1 = new ColaboradorRequest("Julia Teste 1", "54283461090", "00000000", "100", "Casa");
        this.criarColaborador(colaborador1, empresa, conta);

        ColaboradorRequest colaborador2 = new ColaboradorRequest("Julia Teste 2", "16820593026", "00000000", "100", "Casa");
        this.criarColaborador(colaborador2, empresa, conta);

        long resultado = colaboradorRepository.contagemDeColaboradoresAtivosPorCnpj(cnpj);

        assertEquals(2, resultado);
    }
}