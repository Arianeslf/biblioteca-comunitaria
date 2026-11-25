package com.paginavirada.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoCreateRequest(

        @NotNull(message = "O leitor é obrigatório")
        Integer leitorId,

        @NotNull(message = "O livro é obrigatório")
        Integer livroId,

        @NotNull(message = "A data do empréstimo é obrigatória")
        LocalDate dataEmprestimo,

        @NotNull(message = "A data prevista é obrigatória")
        LocalDate dataPrevista

) {
}