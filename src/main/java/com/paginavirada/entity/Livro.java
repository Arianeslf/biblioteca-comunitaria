package com.paginavirada.entity;

import com.paginavirada.entity.enums.CategoriaLivro;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livros")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 200)
    private String titulo;

    @Setter
    @Column(nullable = false, length = 120)
    private String autor;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaLivro categoria;

    @Setter
    @Column(nullable = false, unique = true, length = 17)
    private String isbn;

    @Setter
    @Column(nullable = false)
    private Boolean ativo = true;

    public Livro(String titulo, String autor,
                 CategoriaLivro categoria, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
    }

    @PrePersist
    private void prePersist() {
        this.ativo = true;
    }
}