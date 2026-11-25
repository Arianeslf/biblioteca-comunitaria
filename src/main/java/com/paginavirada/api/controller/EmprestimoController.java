package com.paginavirada.api.controller;

import com.paginavirada.api.dto.filter.EmprestimoFilter;
import com.paginavirada.api.dto.request.EmprestimoCreateRequest;
import com.paginavirada.api.dto.request.EmprestimoStatusRequest;
import com.paginavirada.api.dto.response.EmprestimoResponse;
import com.paginavirada.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoResponse> cadastrar(
            @RequestBody @Valid EmprestimoCreateRequest request) {

        EmprestimoResponse response =
                emprestimoService.cadastrar(request);

        return ResponseEntity
                .created(URI.create("/emprestimos/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponse> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                emprestimoService.buscarPorId(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<EmprestimoResponse>> listar(
            @ModelAttribute EmprestimoFilter filter,
            @PageableDefault(size = 20, sort = "id")
            Pageable pageable) {

        return ResponseEntity.ok(
                emprestimoService.listar(filter, pageable)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmprestimoResponse> alterarStatus(
            @PathVariable Integer id,
            @RequestBody @Valid EmprestimoStatusRequest request) {

        return ResponseEntity.ok(
                emprestimoService.alterarStatus(id, request)
        );
    }
}