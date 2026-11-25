package com.paginavirada.api.dto.request;

import com.paginavirada.entity.enums.StatusEmprestimo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoStatusRequest(

        @NotNull(message = "O status é obrigatório")
        StatusEmprestimo status,

        LocalDate dataDevolucao

) {
}