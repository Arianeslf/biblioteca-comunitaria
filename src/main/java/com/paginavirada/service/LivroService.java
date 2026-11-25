    package com.paginavirada.service;

    import com.paginavirada.api.dto.filter.LivroFilter;
    import com.paginavirada.api.dto.request.LivroCreateRequest;
    import com.paginavirada.api.dto.request.LivroUpdateRequest;
    import com.paginavirada.api.dto.response.LivroResponse;
    import com.paginavirada.entity.Livro;
    import com.paginavirada.repository.LivroRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.server.ResponseStatusException;

    @Service
    @RequiredArgsConstructor
    public class LivroService {

        private final LivroRepository livroRepository;

        @Transactional
        public LivroResponse cadastrar(LivroCreateRequest request) {

            if (livroRepository.existsByIsbn(request.isbn())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Já existe um livro com esse ISBN"
                );
            }

            Livro livro = new Livro(
                    request.titulo(),
                    request.autor(),
                    request.categoria(),
                    request.isbn()
            );

            return new LivroResponse(
                    livroRepository.save(livro)
            );

        }

        @Transactional(readOnly = true)
        public LivroResponse buscarPorId(Integer id) {

            Livro livro = buscarLivro(id);

            return new LivroResponse(livro);
        }

        @Transactional
        public LivroResponse atualizar(
                Integer id, LivroUpdateRequest request) {

            Livro livro = buscarLivro(id);

            if (livroRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Já existe um livro com esse ISBN"
                );
            }

            livro.setTitulo(request.titulo());
            livro.setAutor(request.autor());
            livro.setCategoria(request.categoria());
            livro.setIsbn(request.isbn());
            livro.setAtivo(request.ativo());

            return new LivroResponse(livro);
        }

        @Transactional(readOnly = true)
        public Page<LivroResponse> listar(
                LivroFilter filter, Pageable pageable) {

            Page<Livro> livros;

            boolean temTitulo = filter.titulo() != null
                    && !filter.titulo().isBlank();

            boolean temCategoria = filter.categoria() != null;

            if (temTitulo && temCategoria) {

                livros = livroRepository
                        .findByTituloContainingIgnoreCaseAndCategoria(
                                filter.titulo(),
                                filter.categoria(),
                                pageable
                        );

            } else if (temTitulo) {

                livros = livroRepository
                        .findByTituloContainingIgnoreCase(
                                filter.titulo(), pageable
                        );

            } else if (temCategoria) {

                livros = livroRepository.findByCategoria(
                        filter.categoria(), pageable
                );

            } else {

                livros = livroRepository.findAll(pageable);

            }

            return livros.map(LivroResponse::new);
        }

        private Livro buscarLivro(Integer id) {
            return livroRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Livro não encontrado"
                    ));
        }

    }