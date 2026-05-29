package br.com.sicredi.votacao.domain.model;

import br.com.sicredi.votacao.domain.exception.DomainBusinessException;

public record Cpf(String valor) {
    public Cpf {
        if (valor == null || valor.replaceAll("\\D", "").length() != 11) {
            throw new DomainBusinessException("CPF inválido: deve conter 11 dígitos");
        }
        valor = valor.replaceAll("\\D", "");
    }
}