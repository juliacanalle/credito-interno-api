package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.AtualizaEnderecoRequest;
import io.github.juliacanalle.creditointernoapi.dto.AtualizaNomeRequest;
import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.ColaboradorRequest;
import io.github.juliacanalle.creditointernoapi.exceptions.ColaboradorNotFoundException;
import io.github.juliacanalle.creditointernoapi.exceptions.CpfAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.EmpresaNotFoundException;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.repository.ColaboradorRepository;
import io.github.juliacanalle.creditointernoapi.repository.ContaRepository;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceTest {

    @Mock
    private CepService cepService;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private ColaboradorService colaboradorService;

    @BeforeEach
    void  setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create an empolyee successfully")
    void cadastrarColaboradorComBuscaCep() {

        CepDto cepDto = new CepDto(
                "01001000",
                "Praça da Sé",
                "Apartamento",
                "Sé",
                "São Paulo",
                "SP",
                3550308L,
                1004L,
                11,
                7107L
        );

        ColaboradorRequest colaborador = new ColaboradorRequest("Julia Teste", "00000000000", "01001000", "100", "Casa");

        Empresa empresa = new Empresa();
        empresa.setNome("Empresa XYZ");
        empresa.setCnpj("12345678000199");

        Colaborador colab = new Colaborador();
        colab.setId(1);

        when(cepService.consultaCep(colaborador.cep())).thenReturn(cepDto);
        when(colaboradorRepository.existsByCpf(colaborador.cpf())).thenReturn(false);
        when(empresaRepository.findByCnpj(empresa.getCnpj())).thenReturn(empresa);
        when(validator.validate(any(Colaborador.class))).thenReturn(Collections.emptySet());
        when(validator.validate(any(CepDto.class))).thenReturn(Collections.emptySet());
        when(colaboradorRepository.save(any(Colaborador.class))).thenReturn(colab);

        Colaborador resultado = colaboradorService.cadastrarColaboradorComBuscaCep(colaborador, empresa.getCnpj());

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());

        verify(cepService).consultaCep(colaborador.cep());
        verify(colaboradorRepository).save(any(Colaborador.class));
        verify(empresaRepository).findByCnpj(empresa.getCnpj());
        verify(validator).validate(any(Colaborador.class));
        verify(validator).validate(any(CepDto.class));
        verify(colaboradorRepository).save(any(Colaborador.class));
    }

    @Test
    void atualizarEnderecoColaborador() {

        String cpf = "00000000000";

        CepDto cepDto = new CepDto(
                "01001000",
                "Praça da Sé",
                "Apartamento",
                "Sé",
                "São Paulo",
                "SP",
                3550308L,
                1004L,
                11,
                7107L
        );

        AtualizaEnderecoRequest request = new AtualizaEnderecoRequest("01001000", "100", "Apartamento");

        Colaborador colab = new Colaborador();
        colab.setId(1);
        colab.setCpf(cpf);

        when(colaboradorRepository.findByCpf(colab.getCpf())).thenReturn(colab);
        when(cepService.consultaCep(request.cep())).thenReturn(cepDto);

        colaboradorService.atualizarEnderecoColaborador(request, cpf);

        assertEquals("01001000", colab.getEndereco().getCep());
        assertEquals("100", colab.getEndereco().getNumero());
        assertEquals("Apartamento", colab.getEndereco().getComplemento());

        verify(colaboradorRepository).findByCpf(colab.getCpf());
        verify(colaboradorRepository).save(colab);
    }

    @Test
    void atualizarNomeColaborador() {
        String novoNome = "Julia Teste";
        String cpf = "00000000000";

        AtualizaNomeRequest request = new AtualizaNomeRequest("Julia Teste");

        Colaborador colab = new Colaborador();
        colab.setId(1);
        colab.setCpf(cpf);

        when(colaboradorRepository.findByCpf(colab.getCpf())).thenReturn(colab);

        colaboradorService.atualizarNomeColaborador(cpf, novoNome);

        assertEquals("00000000000", colab.getCpf());
        assertEquals("Julia Teste", colab.getNome());

        verify(colaboradorRepository).findByCpf(colab.getCpf());
        verify(colaboradorRepository).save(colab);
    }

    @Test
    void inativarColaborador() {
        String cpf = "00000000000";

        Colaborador colab = new Colaborador();
        colab.setId(1);
        colab.setCpf(cpf);

        when(colaboradorRepository.findByCpf(colab.getCpf())).thenReturn(colab);

        colaboradorService.inativarColaborador(colab.getCpf());

        verify(colaboradorRepository).findByCpf(colab.getCpf());
        verify(colaboradorRepository).inativarColaborador(colab.getCpf());
    }

    @Test
    void listarColaboradoresPorEmpresa() {

        String cpfColab1 = "00000000001";
        String cpfColab2 = "00000000002";
        String cpfColab3 = "00000000003";

        Colaborador colab1 = new Colaborador();
        Colaborador colab2 = new Colaborador();
        Colaborador colab3 = new Colaborador();

        colab1.setId(1);
        colab2.setId(2);
        colab3.setId(3);

        colab1.setCpf(cpfColab1);
        colab2.setCpf(cpfColab2);
        colab3.setCpf(cpfColab3);

        Empresa empresa = new Empresa();
        empresa.setNome("Empresa XYZ");
        empresa.setCnpj("12345678000199");

        colab1.setEmpresa(empresa);
        colab2.setEmpresa(empresa);
        colab3.setEmpresa(empresa);

        when(empresaRepository.findByCnpj(empresa.getCnpj())).thenReturn(empresa);

        colaboradorService.listarColaboradoresPorEmpresa(empresa.getCnpj());

        verify(empresaRepository).findByCnpj(empresa.getCnpj());
        verify(colaboradorRepository).findAllByEmpresaAndAtivoTrue(empresa);
    }

    @Test
    void deveLancarCpfAlreadyExistsExceptionQuandoCpfExistente() {
        String cpf = "00000000000";
        ColaboradorRequest request = new ColaboradorRequest(
                "Julia Teste", cpf, "01001000", "100", "Complemento"
        );

        Empresa empresa = new Empresa();
        empresa.setNome("Empresa XYZ");
        empresa.setCnpj("12345678000199");

        when(colaboradorRepository.existsByCpf(cpf)).thenReturn(true);
        when(cepService.consultaCep("01001000")).thenReturn(new CepDto(
                "01001000", "Praça da Sé", "Complemento", "Sé", "São Paulo", "SP",
                3550308L, 1004L, 11, 7107L
        ));

        assertThrows(CpfAlreadyExistsException.class, () -> {
            colaboradorService.cadastrarColaboradorComBuscaCep(request, empresa.getCnpj());
        });
    }

    @Test
    void deveLancarEmpresaNotFoundExceptionQuandoEmpresaNaoEncontrada() {
        String cpf = "00000000000";
        ColaboradorRequest request = new ColaboradorRequest(
                "Julia Teste", cpf, "01001000", "100", "Complemento"
        );

        Empresa empresa = new Empresa();
        empresa.setNome("Empresa XYZ");
        empresa.setCnpj("12345678000199");

        when(empresaRepository.findByCnpj(empresa.getCnpj())).thenReturn(null);
        when(cepService.consultaCep("01001000")).thenReturn(new CepDto(
                "01001000", "Praça da Sé", "Complemento", "Sé", "São Paulo", "SP",
                3550308L, 1004L, 11, 7107L
        ));

        assertThrows(EmpresaNotFoundException.class, () -> {
            colaboradorService.cadastrarColaboradorComBuscaCep(request, empresa.getCnpj());
        });
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoAtualizarEnderecoEColaboradorNaoExistir() {
        String cpf = "00000000000";
        AtualizaEnderecoRequest req = new AtualizaEnderecoRequest("00000000", "100", "Casa");
        when(colaboradorRepository.findByCpf(cpf)).thenReturn(null);

        assertThrows(
                ColaboradorNotFoundException.class,
                () -> colaboradorService.atualizarEnderecoColaborador(req, cpf)
        );
        verify(colaboradorRepository, never()).save(any());
    }

    @Test
    void deveLancarColaboradorNotFoundExceptionQuandoAtualizarNomeEColaboradorNaoExistir() {
        String cpf = "00000000000";
        String novoNome = "Julia Teste";
        when(colaboradorRepository.findByCpf(cpf)).thenReturn(null);

        assertThrows(
                ColaboradorNotFoundException.class,
                () -> colaboradorService.atualizarNomeColaborador(novoNome, cpf)
        );
        verify(colaboradorRepository, never()).save(any());
    }
}


