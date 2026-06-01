package br.com.sicredi.votacao.domain.model;

public class Associado {
    private final Cpf cpf;

    public Associado(Cpf cpf) {
        this.cpf = cpf;
    }

    public Cpf cpf() { return cpf; }
}
