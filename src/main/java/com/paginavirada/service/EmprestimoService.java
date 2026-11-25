package com.paginavirada.service;

import com.paginavirada.api.dto.filter.EmprestimoFilter;
import com.paginavirada.api.dto.request.EmprestimoCreateRequest;
import com.paginavirada.api.dto.request.EmprestimoStatusRequest;
import com.paginavirada.api.dto.response.EmprestimoResponse;
import com.paginavirada.entity.Emprestimo;
import com.paginavirada.entity.Leitor;
import com.paginavirada.entity.Livro;
import com.paginavirada.entity.enums.StatusEmprestimo;
import com.paginavirada.repository.EmprestimoRepository;
import com.paginavirada.repository.LeitorRepository;
import com.paginavirada.repository.LivroRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LeitorRepository leitorRepository;
    private final LivroRepository livroRepository;

    @Transactional
    public EmprestimoResponse cadastrar(
            EmprestimoCreateRequest request) {

        Leitor leitor = leitorRepository
                .findById(request.leitorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Leitor não encontrado"
                ));

        Livro livro = livroRepository
                .findById(request.livroId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado"
                ));

        validarDisponibilidade(leitor, livro);

        validarDatas(
                request.dataEmprestimo(),
                request.dataPrevista()
        );

        Emprestimo emprestimo = new Emprestimo(
                leitor,
                livro,
                request.dataEmprestimo(),
                request.dataPrevista()
        );

        return new EmprestimoResponse(
                emprestimoRepository.save(emprestimo)
        );
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscarPorId(Integer id) {

        Emprestimo emprestimo = emprestimoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empréstimo não encontrado"
                ));

        return new EmprestimoResponse(emprestimo);
    }

    @Transactional(readOnly = true)
    public Page<EmprestimoResponse> listar(
            EmprestimoFilter filter,
            Pageable pageable) {

        Specification<Emprestimo> specification =
                (root, query, cb) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    if (filter.status() != null) {
                        predicates.add(
                                cb.equal(root.get("status"), filter.status())
                        );
                    }

                    if (filter.leitorId() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("leitor").get("id"),
                                        filter.leitorId()
                                )
                        );
                    }

                    if (filter.livroId() != null) {
                        predicates.add(
                                cb.equal(
                                        root.get("livro").get("id"),
                                        filter.livroId()
                                )
                        );
                    }

                    if (filter.dataInicio() != null) {
                        predicates.add(
                                cb.greaterThanOrEqualTo(
                                        root.<LocalDate>get("dataEmprestimo"),
                                        filter.dataInicio()
                                )
                        );
                    }

                    if (filter.dataFim() != null) {
                        predicates.add(
                                cb.lessThanOrEqualTo(
                                        root.<LocalDate>get("dataEmprestimo"),
                                        filter.dataFim()
                                )
                        );
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                };

        return emprestimoRepository
                .findAll(specification, pageable)
                .map(EmprestimoResponse::new);
    }

    @Transactional
    public EmprestimoResponse alterarStatus(
            Integer id,
            EmprestimoStatusRequest request) {

        Emprestimo emprestimo = emprestimoRepository
                .buscarPorIdComBloqueio(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empréstimo não encontrado"
                ));

        validarTransicaoStatus(emprestimo, request.status());

        if (request.status() == StatusEmprestimo.DEVOLVIDO) {

            validarDataDevolucao(
                    emprestimo,
                    request.dataDevolucao()
            );

            emprestimo.setDataDevolucao(request.dataDevolucao());

        } else if (request.status() == StatusEmprestimo.CANCELADO) {

            emprestimo.setDataDevolucao(null);
        }

        emprestimo.setStatus(request.status());

        return new EmprestimoResponse(emprestimo);
    }

    private void validarDisponibilidade(Leitor leitor, Livro livro) {

        if (!leitor.getAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O leitor está inativo"
            );
        }

        if (!livro.getAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O livro está inativo"
            );
        }

        if (emprestimoRepository.existsByLivroIdAndStatus(
                livro.getId(), StatusEmprestimo.ABERTO)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O livro já possui um empréstimo aberto"
            );
        }
    }

    private void validarDatas(
            LocalDate dataEmprestimo,
            LocalDate dataPrevista) {

        if (dataPrevista.isBefore(dataEmprestimo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data prevista não pode ser anterior à data do empréstimo"
            );
        }

        if (dataPrevista.isAfter(dataEmprestimo.plusDays(30))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O prazo máximo do empréstimo é de 30 dias"
            );
        }
    }

    private void validarTransicaoStatus(
            Emprestimo emprestimo,
            StatusEmprestimo novoStatus) {

        if (emprestimo.getStatus() != StatusEmprestimo.ABERTO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Somente empréstimos abertos podem ter o status alterado"
            );
        }

        if (novoStatus == StatusEmprestimo.ABERTO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transição de status inválida"
            );
        }
    }

    private void validarDataDevolucao(
            Emprestimo emprestimo,
            LocalDate dataDevolucao) {

        if (dataDevolucao == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de devolução é obrigatória"
            );
        }

        if (dataDevolucao.isBefore(emprestimo.getDataEmprestimo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A data de devolução não pode ser anterior à data do empréstimo"
            );
        }
    }
}