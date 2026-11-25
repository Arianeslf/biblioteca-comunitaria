package com.paginavirada.api.dto.filter;

import com.paginavirada.entity.enums.CategoriaLivro;

public record LivroFilter(

        String titulo,
        CategoriaLivro categoria

) {
}