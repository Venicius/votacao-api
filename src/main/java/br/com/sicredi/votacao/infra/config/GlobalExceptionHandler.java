package br.com.sicredi.votacao.infra.config;

import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainBusinessException.class)
    public ResponseEntity<Map<String, String>> handleDomainBusinessException(DomainBusinessException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.internalServerError().body(Map.of("erro", ex.getMessage()));
    }
}