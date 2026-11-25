
package com.paginavirada.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.data.core.PropertyReferenceException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraNegocio(
            ResponseStatusException exception) {

        Map<String, Object> erro = new LinkedHashMap<>();

        erro.put("status", exception.getStatusCode().value());
        erro.put("message", exception.getReason());

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> campos = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(
                        erro.getField(),
                        erro.getDefaultMessage()
                ));

        Map<String, Object> resposta = new LinkedHashMap<>();

        resposta.put("status", 400);
        resposta.put("message", "Dados inválidos");
        resposta.put("errors", campos);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonInvalido(
            HttpMessageNotReadableException exception) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "message", "Corpo da requisição inválido"
                )
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarIntegridade(
            DataIntegrityViolationException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "status", 409,
                        "message", "Operação viola uma restrição de integridade dos dados"
                )
        );
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> tratarOrdenacaoInvalida(
            PropertyReferenceException exception) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "message", "Campo de ordenação inválido: "
                                + exception.getPropertyName()
                )
        );
    }
}
