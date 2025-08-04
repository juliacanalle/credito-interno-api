package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.dto.CepDto;
import io.github.juliacanalle.creditointernoapi.dto.DadosAtualizaCadastroEmpresa;
import io.github.juliacanalle.creditointernoapi.dto.EmpresaRequest;
import io.github.juliacanalle.creditointernoapi.exceptions.AtLeastOneFieldPresentException;
import io.github.juliacanalle.creditointernoapi.exceptions.CnpjAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.EmpresaAlreadyExistsException;
import io.github.juliacanalle.creditointernoapi.exceptions.InactiveEmpresaException;
import io.github.juliacanalle.creditointernoapi.model.Colaborador;
import io.github.juliacanalle.creditointernoapi.model.Empresa;
import io.github.juliacanalle.creditointernoapi.repository.EmpresaRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private CepService cepService;

    @Mock
    private Validator validator;

    @Test
    void cadastrarEmpresaComBuscaCep() {

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

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setCnpj(request.cnpj());

        when(cepService.consultaCep(request.cep())).thenReturn(cepDto);
        when(empresaRepository.existsByCnpj(request.cnpj())).thenReturn(false);
        when(validator.validate(any(Empresa.class))).thenReturn(Collections.emptySet());
        when(validator.validate(any(CepDto.class))).thenReturn(Collections.emptySet());
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        empresaService.cadastrarEmpresaComBuscaCep(request);

        verify(cepService).consultaCep(request.cep());
        verify(empresaRepository).save(any(Empresa.class));
        verify(validator).validate(any(Empresa.class));
        verify(validator).validate(any(CepDto.class));
    }

    @Test
    void deveAtualizarApenasNomeEmpresa() {

        String cnpjAtual = "12345678000199";

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setCnpj(request.cnpj());

        DadosAtualizaCadastroEmpresa dados = new DadosAtualizaCadastroEmpresa("Empresa World", null);

        when(empresaRepository.findByCnpj(request.cnpj())).thenReturn(empresa);

        empresaService.atualizarEmpresa(request.cnpj(), dados);

        verify(empresaRepository).atualizarNome(cnpjAtual, "Empresa World");
    }

    @Test
    void deveAtualizarApenasCnpjEmpresa() {

        String cnpjAtual = "12345678000199";
        String cnpjNovo = "98765432000100";

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setCnpj(request.cnpj());

        DadosAtualizaCadastroEmpresa dados = new DadosAtualizaCadastroEmpresa(null, cnpjNovo);

        when(empresaRepository.findByCnpj(request.cnpj())).thenReturn(empresa);
        when(empresaRepository.findByCnpj(cnpjNovo)).thenReturn(null);

        empresaService.atualizarEmpresa(request.cnpj(), dados);

        verify(empresaRepository).atualizarCnpj(cnpjAtual, cnpjNovo);
    }

    @Test
    void deveAtualizarNomeCnpjEmpresa() {

        String cnpjAtual = "12345678000199";
        String cnpjNovo = "98765432000100";

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setCnpj(request.cnpj());

        DadosAtualizaCadastroEmpresa dados = new DadosAtualizaCadastroEmpresa("Empresa World", cnpjNovo);

        when(empresaRepository.findByCnpj(request.cnpj())).thenReturn(empresa);
        when(empresaRepository.findByCnpj(cnpjNovo)).thenReturn(null);

        empresaService.atualizarEmpresa(request.cnpj(), dados);

        verify(empresaRepository).atualizarCnpj(cnpjAtual, cnpjNovo);
        verify(empresaRepository).atualizarNome(cnpjAtual, "Empresa World");
    }

    @Test
    void inativarEmpresa() {

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setCnpj(request.cnpj());

        when(empresaRepository.findByCnpj(request.cnpj())).thenReturn(empresa);

        empresaService.inativarEmpresa(request.cnpj());

        verify(empresaRepository).inativarEmpresa(request.cnpj());
    }

    @Test
    void deveLancarEmpresaAlreadyExistsExceptionQuandoEmpresaExistenteCadastro() {

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setCnpj(request.cnpj());

        when(empresaRepository.existsByCnpj("12345678000199"))
                .thenReturn(true);

        assertThrows(EmpresaAlreadyExistsException.class, () -> {
            empresaService.cadastrarEmpresaComBuscaCep(request);
        });

        verify(empresaRepository, never()).save(any());
        verify(cepService, never()).consultaCep(any());
    }

    @Test
    void deveLancarAtLeastOneFieldPresentExceptionQuandoNenhumCampoForPreenchidoAtualizarEmpresa() {

        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setCnpj(request.cnpj());

        DadosAtualizaCadastroEmpresa dados = new DadosAtualizaCadastroEmpresa(null, null);

        when(empresaRepository.findByCnpj(request.cnpj())).thenReturn(empresa);

        assertThrows(AtLeastOneFieldPresentException.class, () -> {
            empresaService.atualizarEmpresa(empresa.getCnpj(), dados);
        });
    }

    @Test
    void deveLancarCnpjAlreadyExistsExceptionQuandoEmpresaExistenteAtualizarEmpresa() {
        EmpresaRequest request = new EmpresaRequest("Empresa XYZ", "12345678000199", "01001000", "100", "Empresa");

        Empresa empresa = new Empresa();
        empresa.setId(1);
        empresa.setCnpj(request.cnpj());

        DadosAtualizaCadastroEmpresa dados = new DadosAtualizaCadastroEmpresa(null, "12345678000120");

        when(empresaRepository.findByCnpj(empresa.getCnpj())).thenReturn(empresa);
        when(empresaRepository.findByCnpj("12345678000120")).thenReturn(new Empresa());

        assertThrows(CnpjAlreadyExistsException.class, () -> {
            empresaService.atualizarEmpresa(empresa.getCnpj(), dados);
        });

        verify(empresaRepository, never()).atualizarCnpj(any(), any());
        verify(empresaRepository, never()).atualizarNome(any(), any());
    }
}
