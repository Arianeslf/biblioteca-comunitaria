package com.paginavirada.api.controller;

import com.paginavirada.service.ExportacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/exportacoes")
@RequiredArgsConstructor
public class ExportacaoController {

    private final ExportacaoService exportacaoService;

    @GetMapping("/emprestimos")
    public ResponseEntity<byte[]> exportarEmprestimos() {

        String csv = exportacaoService.exportarEmprestimos();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=emprestimos.csv"
                )
                .contentType(
                        MediaType.parseMediaType("text/csv; charset=UTF-8")
                )
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }
}