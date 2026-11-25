package com.paginavirada.repository;

import com.paginavirada.entity.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LeitorRepository extends
        JpaRepository<Leitor, Integer>,
        JpaSpecificationExecutor<Leitor> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

}
