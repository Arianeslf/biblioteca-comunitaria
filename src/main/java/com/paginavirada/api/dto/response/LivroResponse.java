package com.paginavirada.api.dto.response;

import com.paginavirada.entity.Livro;
import com.paginavirada.entity.enums.CategoriaLivro;

public record LivroResponse(

        Integer id,
        String titulo,
        String autor,
        CategoriaLivro categoria,
        String isbn,
        Boolean ativo

) {

    public LivroResponse(Livro livro) {
        this(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getCategoria(),
                livro.getIsbn(),
                livro.getAtivo()
        );
    }
}