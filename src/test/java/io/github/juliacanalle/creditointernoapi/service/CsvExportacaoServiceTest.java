package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.enums.TipoTransacao;
import io.github.juliacanalle.creditointernoapi.model.Transacao;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CsvExportacaoServiceTest {

        @Test
        void exportarTransacoes_deveConfigurarCabecalhosECorpoCSV() throws IOException {
            String cpf = "00000000000";
            CsvExportacaoService service = new CsvExportacaoService();

            Transacao t1 = new Transacao();
            t1.setCriadoEm(LocalDateTime.of(2025, 8, 31, 10, 30));
            t1.setTipoTransacao(TipoTransacao.CREDITO);
            t1.setValor(new BigDecimal("1500"));
            t1.setMensagem("Primeira");

            Transacao t2 = new Transacao();
            t2.setCriadoEm(LocalDateTime.of(2025, 8, 31, 11,  0));
            t2.setTipoTransacao(TipoTransacao.DEBITO);
            t2.setValor(new BigDecimal("500"));
            t2.setMensagem("Segunda");

            List<Transacao> transacoes = List.of(t1, t2);
            MockHttpServletResponse response = new MockHttpServletResponse();

            service.exportarTransacoes(transacoes, response, cpf);

            assertEquals("text/csv;charset=UTF-8", response.getContentType());
            assertEquals("UTF-8",   response.getCharacterEncoding());
            assertEquals(
                    "attachment; filename=\"extrato_" + cpf + ".csv\"",
                    response.getHeader("Content-Disposition")
            );

            String csv = response.getContentAsString(StandardCharsets.UTF_8);

            assertTrue(csv.startsWith("\uFEFF"), "CSV deve começar com BOM (Byte Order Mark)");

            String[] linhas = csv.split("\\r?\\n");

            assertEquals("\uFEFFData,Tipo,Valor,Descrição", linhas[0]);

            assertEquals("2025-08-31T10:30,CREDITO,1500,Primeira", linhas[1]);
            assertEquals("2025-08-31T11:00,DEBITO,500,Segunda",   linhas[2]);

            assertEquals(3, linhas.length);
        }
}
