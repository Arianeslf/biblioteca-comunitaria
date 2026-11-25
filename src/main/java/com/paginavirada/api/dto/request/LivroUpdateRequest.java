package com.paginavirada.api.dto.request;

import com.paginavirada.entity.enums.CategoriaLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroUpdateRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(min = 1, max = 200)
        String titulo,

        @NotBlank(message = "O autor é obrigatório")
        @Size(min = 3, max = 120)
        String autor,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaLivro categoria,

        @NotBlank(message = "O ISBN é obrigatório")
        @Size(min = 10, max = 17)
        String isbn,

        @NotNull(message = "O campo ativo é obrigatório")
        Boolean ativo

) {
}