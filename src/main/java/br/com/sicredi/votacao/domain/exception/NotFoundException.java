package br.com.sicredi.votacao.domain.exception;

public class NotFoundException extends DomainBusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}
