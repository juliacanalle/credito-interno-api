package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.model.Transacao;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvExportacaoService {

    public void exportarTransacoes(List<Transacao> transacoes, HttpServletResponse response, String cpf) throws IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"extrato_" + cpf + ".csv\"");

        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        writer.write('\uFEFF'); // <-- BOM (Byte Order Mark)

        writer.write("Data,Tipo,Valor,Descrição\n");

        for (Transacao t : transacoes) {
            writer.write(String.format(
                    "%s,%s,%s,%s\n",
                    t.getCriadoEm(),
                    t.getTipoTransacao(),
                    t.getValor(),
                    t.getMensagem()
            ));
        }

        writer.flush();
    }
}
