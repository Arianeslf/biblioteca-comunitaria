package com.paginavirada.api.controller;

import com.paginavirada.api.dto.filter.LeitorFilter;
import com.paginavirada.api.dto.request.LeitorCreateRequest;
import com.paginavirada.api.dto.request.LeitorUpdateRequest;
import com.paginavirada.api.dto.response.LeitorResponse;
import com.paginavirada.service.LeitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/leitores")
@RequiredArgsConstructor
public class LeitorController {

    private final LeitorService leitorService;


    @PostMapping
    public ResponseEntity<LeitorResponse> cadastrar(
            @RequestBody @Valid LeitorCreateRequest request) {

        LeitorResponse response = leitorService.cadastrar(request);

        URI location = URI.create("/leitores/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeitorResponse> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                leitorService.buscarPorId(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<LeitorResponse> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid LeitorUpdateRequest request) {

        return ResponseEntity.ok(
                leitorService.atualizar(id, request)
        );
    }


    @GetMapping
    public ResponseEntity<Page<LeitorResponse>> listar(
            @ModelAttribute LeitorFilter filter,
            @PageableDefault(size = 20, sort = "id")
            Pageable pageable) {

        return ResponseEntity.ok(
                leitorService.listar(filter, pageable)
        );
    }


}
