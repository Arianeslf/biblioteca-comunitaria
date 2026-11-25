package com.paginavirada.entity;

import com.paginavirada.entity.enums.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leitor_id", nullable = false)
    private Leitor leitor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Setter
    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo status = StatusEmprestimo.ABERTO;

    public Emprestimo(Leitor leitor, Livro livro,
                      LocalDate dataEmprestimo,
                      LocalDate dataPrevista) {

        this.leitor = leitor;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
    }

    @PrePersist
    private void prePersist() {
        this.status = StatusEmprestimo.ABERTO;
        this.dataDevolucao = null;
    }
}