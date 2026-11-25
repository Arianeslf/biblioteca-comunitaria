package com.paginavirada.api.dto.filter;

import com.paginavirada.entity.enums.StatusEmprestimo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record EmprestimoFilter(

        StatusEmprestimo status,

        Integer leitorId,

        Integer livroId,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicio,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFim

) {
}