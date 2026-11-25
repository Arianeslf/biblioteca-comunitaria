package com.paginavirada.service;

import com.paginavirada.entity.Emprestimo;
import com.paginavirada.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExportacaoService {

    private final EmprestimoRepository emprestimoRepository;

    @Transactional(readOnly = true)
    public String exportarEmprestimos() {

        List<Emprestimo> emprestimos =
                emprestimoRepository.findAllByOrderByIdAsc();

        StringBuilder csv = new StringBuilder();

        csv.append("emprestimo_id,leitor_id,leitor_nome,cidade,")
                .append("livro_id,livro_titulo,autor,categoria,")
                .append("data_emprestimo,data_prevista,")
                .append("data_devolucao,status\r\n");

        for (Emprestimo emprestimo : emprestimos) {

            String linha = Stream.of(
                            emprestimo.getId(),
                            emprestimo.getLeitor().getId(),
                            emprestimo.getLeitor().getNome(),
                            emprestimo.getLeitor().getCidade(),
                            emprestimo.getLivro().getId(),
                            emprestimo.getLivro().getTitulo(),
                            emprestimo.getLivro().getAutor(),
                            emprestimo.getLivro().getCategoria(),
                            emprestimo.getDataEmprestimo(),
                            emprestimo.getDataPrevista(),
                            emprestimo.getDataDevolucao(),
                            emprestimo.getStatus()
                    )
                    .map(this::formatarCampo)
                    .collect(Collectors.joining(","));

            csv.append(linha).append("\r\n");
        }

        return csv.toString();
    }

    private String formatarCampo(Object valor) {

        if (valor == null) {
            return "";
        }

        String texto = valor.toString();

        return "\"" + texto.replace("\"", "\"\"") + "\"";
    }
}