package com.paginavirada.api.dto.response;

import com.paginavirada.entity.Emprestimo;
import com.paginavirada.entity.enums.StatusEmprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(

        Integer id,
        Integer leitorId,
        String leitorNome,
        Integer livroId,
        String livroTitulo,
        LocalDate dataEmprestimo,
        LocalDate dataPrevista,
        LocalDate dataDevolucao,
        StatusEmprestimo status

) {

    public EmprestimoResponse(Emprestimo emprestimo) {
        this(
                emprestimo.getId(),
                emprestimo.getLeitor().getId(),
                emprestimo.getLeitor().getNome(),
                emprestimo.getLivro().getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevista(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatus()
        );
    }
}