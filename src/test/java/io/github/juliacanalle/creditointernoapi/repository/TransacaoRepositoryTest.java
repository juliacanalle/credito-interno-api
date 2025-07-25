package io.github.juliacanalle.creditointernoapi.repository;

import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.model.*;
import io.github.juliacanalle.creditointernoapi.service.ContaService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class TransacaoRepositoryTest {

    @Autowired
    TransacaoRepository transacaoRepository;

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

    private Colaborador criarColaborador(ColaboradorRequest request, Empresa empresa, Conta conta) {

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(request.nome());
        colaborador.setCpf(request.cpf());
        colaborador.setEndereco(enderecoPadrao());
        colaborador.setEmpresa(empresa);
        colaborador.setConta(conta);

        entityManager.persist(colaborador);

        return colaborador;
    }

    @Test
    void shouldReturnTransactionsWithinDateAndValueRangePageSuccess() {
        LocalDateTime agora = LocalDateTime.now();

        Conta conta = new Conta();
        entityManager.persist(conta);

        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        ColaboradorRequest colaborador = new ColaboradorRequest("Julia Teste", "54239214977", "0713400", "100", "Casa");
        this.criarColaborador(colaborador, empresa, conta);

        Transacao t1 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Agosto/2025", TipoTransacao.CREDITO, new BigDecimal("1000.00"), agora);
        Transacao t2 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Setembro/2025", TipoTransacao.CREDITO, new BigDecimal("2000.00"), agora);
        Transacao t3 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Outubro/2025", TipoTransacao.CREDITO, new BigDecimal("2500.00"), agora);
        Transacao t4 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Novembro/2025", TipoTransacao.CREDITO, new BigDecimal("3000.00"), agora);
        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(t3);
        entityManager.persist(t4);

        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Transacao> transacoes = transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(conta, agora, agora, new BigDecimal("1000.00"), new BigDecimal("2500.00"), pageable);

        assertEquals(3, transacoes.getTotalElements());
        assertEquals("54239214977", colaborador.cpf());
    }

    @Test
    void shouldReturnTransactionsWithinDateAndValueRangeListSuccess() {
        LocalDateTime agora = LocalDateTime.now();

        Conta conta = new Conta();
        entityManager.persist(conta);

        Empresa empresa = new Empresa("Pluxee", "12345678000199", enderecoPadrao());
        entityManager.persist(empresa);

        ColaboradorRequest colaborador = new ColaboradorRequest("Julia Teste", "54239214977", "0713400", "100", "Casa");
        this.criarColaborador(colaborador, empresa, conta);

        Transacao t1 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Agosto/2025", TipoTransacao.CREDITO, new BigDecimal("1000.00"), agora);
        Transacao t2 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Setembro/2025", TipoTransacao.CREDITO, new BigDecimal("2000.00"), agora);
        Transacao t3 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Outubro/2025", TipoTransacao.CREDITO, new BigDecimal("2500.00"), agora);
        Transacao t4 = new Transacao(conta, new BigDecimal("100.00"), "Crédito VR Novembro/2025", TipoTransacao.CREDITO, new BigDecimal("3000.00"), agora);
        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(t3);
        entityManager.persist(t4);

        entityManager.flush();
        entityManager.clear();

        Sort sort = Sort.by(Sort.Order.asc("valor"));

        List<Transacao> transacoes = transacaoRepository.findByContaAndCriadoEmBetweenAndValorBetween(conta, agora, agora, new BigDecimal("1000.00"), new BigDecimal("2500.00"),sort);

        assertEquals(3, transacoes.size());
        assertEquals("54239214977", colaborador.cpf());
    }
}
