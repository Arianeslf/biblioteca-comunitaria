package com.paginavirada.repository;

import com.paginavirada.entity.Emprestimo;
import com.paginavirada.entity.enums.StatusEmprestimo;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;


@Repository
public interface EmprestimoRepository extends
        JpaRepository<Emprestimo, Integer>,
        JpaSpecificationExecutor<Emprestimo> {

    boolean existsByLivroIdAndStatus(
            Integer livroId,
            StatusEmprestimo status
    );

    @Override
    @EntityGraph(attributePaths = {"leitor", "livro"})
    Page<Emprestimo> findAll(
            Specification<Emprestimo> specification,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Emprestimo e WHERE e.id = :id")
    Optional<Emprestimo> buscarPorIdComBloqueio(
            @Param("id") Integer id
    );

    @EntityGraph(attributePaths = {"leitor", "livro"})
    List<Emprestimo> findAllByOrderByIdAsc();
}