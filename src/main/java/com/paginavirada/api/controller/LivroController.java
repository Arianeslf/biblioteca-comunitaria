package com.paginavirada.api.controller;

import com.paginavirada.api.dto.filter.LivroFilter;
import com.paginavirada.api.dto.request.LivroCreateRequest;
import com.paginavirada.api.dto.request.LivroUpdateRequest;
import com.paginavirada.api.dto.response.LivroResponse;
import com.paginavirada.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(
            @RequestBody @Valid LivroCreateRequest request) {

        LivroResponse response = livroService.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(
            @PathVariable Integer id) {

        LivroResponse response = livroService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid LivroUpdateRequest request) {

        LivroResponse response = livroService.atualizar(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<LivroResponse>> listar(
            @ModelAttribute LivroFilter filter,
            @PageableDefault(page = 0, size = 20, sort = "id")
            Pageable pageable) {

        Page<LivroResponse> response =
                livroService.listar(filter, pageable);

        return ResponseEntity.ok(response);
    }
}