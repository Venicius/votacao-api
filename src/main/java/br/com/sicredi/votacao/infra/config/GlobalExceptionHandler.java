package br.com.sicredi.votacao.infra.config;

import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(DomainBusinessException.class)
    public ResponseEntity<Map<String, String>> handleDomainBusinessException(DomainBusinessException ex) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("erro", ex.getMessage()));
    }
}