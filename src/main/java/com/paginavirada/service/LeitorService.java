
package com.paginavirada.service;

import com.paginavirada.api.dto.filter.LeitorFilter;
import com.paginavirada.api.dto.request.LeitorCreateRequest;
import com.paginavirada.api.dto.request.LeitorUpdateRequest;
import com.paginavirada.api.dto.response.LeitorResponse;
import com.paginavirada.entity.Leitor;
import com.paginavirada.repository.LeitorRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LeitorService {

    private final LeitorRepository leitorRepository;

    @Transactional
    public LeitorResponse cadastrar(LeitorCreateRequest request) {

        if (leitorRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um leitor com esse e-mail"
            );
        }

        Leitor leitor = new Leitor(
                request.nome(),
                request.email(),
                request.telefone(),
                request.cidade()
        );

        Leitor leitorSalvo = leitorRepository.save(leitor);

        return new LeitorResponse(leitorSalvo);
    }

    @Transactional(readOnly = true)
    public LeitorResponse buscarPorId(Integer id) {
        Leitor leitor = buscarLeitor(id);

        return new LeitorResponse(leitor);
    }


    @Transactional
    public LeitorResponse atualizar(
            Integer id, LeitorUpdateRequest request) {

        Leitor leitor = buscarLeitor(id);

        if (leitorRepository.existsByEmailAndIdNot(
                request.email(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro leitor com esse e-mail"
            );
        }

        leitor.setNome(request.nome());
        leitor.setEmail(request.email());
        leitor.setTelefone(request.telefone());
        leitor.setCidade(request.cidade());
        leitor.setAtivo(request.ativo());

        return new LeitorResponse(leitor);
    }


    @Transactional(readOnly = true)
    public Page<LeitorResponse> listar(
            LeitorFilter filter, Pageable pageable) {

        Specification<Leitor> specification =
                (root, query, cb) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    if (filter.nome() != null
                            && !filter.nome().isBlank()) {

                        String nome = filter.nome()
                                .toLowerCase(Locale.ROOT)
                                .replace("\\", "\\\\")
                                .replace("%", "\\%")
                                .replace("_", "\\_");

                        predicates.add(
                                cb.like(
                                        cb.lower(root.<String>get("nome")),
                                        "%" + nome + "%",
                                        '\\'
                                )
                        );
                    }

                    if (filter.cidade() != null
                            && !filter.cidade().isBlank()) {

                        predicates.add(
                                cb.equal(
                                        root.get("cidade"),
                                        filter.cidade()
                                )
                        );
                    }

                    return cb.and(
                            predicates.toArray(new Predicate[0])
                    );
                };

        return leitorRepository
                .findAll(specification, pageable)
                .map(LeitorResponse::new);
    }

    private Leitor buscarLeitor(Integer id) {
        return leitorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Leitor não encontrado"
                ));
    }

}
