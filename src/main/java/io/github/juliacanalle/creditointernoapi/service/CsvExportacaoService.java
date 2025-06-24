package io.github.juliacanalle.creditointernoapi.service;

import io.github.juliacanalle.creditointernoapi.model.Transacao;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvExportacaoService {

    public void exportarTransacoes(List<Transacao> transacoes, HttpServletResponse response, String cpf) throws IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"extrato_" + cpf + ".csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("Data,Tipo,Valor,Descrição");

        for (Transacao t : transacoes) {
            writer.printf("%s,%s,%s,%s%n",
                    t.getCriadoEm(),
                    t.getTipoTransacao(),
                    t.getValor(),
                    t.getMensagem());
        }

        writer.flush();
    }
}
