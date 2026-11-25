package com.paginavirada.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "leitores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Leitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 120)
    private String nome;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, length = 11)
    private String telefone;

    @Setter
    @Column(nullable = false, length = 80)
    private String cidade;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Setter
    @Column(nullable = false)
    private Boolean ativo = true;

    public Leitor(String nome, String email,
                  String telefone, String cidade) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
    }

    @PrePersist
    private void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }
}
