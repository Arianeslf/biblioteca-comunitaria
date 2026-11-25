package com.paginavirada.repository;

import com.paginavirada.entity.Livro;
import com.paginavirada.entity.enums.CategoriaLivro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository
        extends JpaRepository<Livro, Integer> {

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Integer id);

    Page<Livro> findByTituloContainingIgnoreCase(
            String titulo, Pageable pageable);

    Page<Livro> findByCategoria(
            CategoriaLivro categoria, Pageable pageable);

    Page<Livro> findByTituloContainingIgnoreCaseAndCategoria(
            String titulo, CategoriaLivro categoria,
            Pageable pageable);

}