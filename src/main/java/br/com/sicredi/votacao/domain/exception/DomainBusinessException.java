package br.com.sicredi.votacao.domain.exception;

public class DomainBusinessException extends RuntimeException {
    public DomainBusinessException(String message) {
        super(message);
    }
}