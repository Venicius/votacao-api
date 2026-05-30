package br.com.sicredi.votacao.domain.model;

import java.util.Objects;

public class Associado {
    private final Cpf cpf;

    public Associado(Cpf cpf) {
        this.cpf = cpf;
    }

    public Cpf cpf() { return cpf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Associado associado = (Associado) o;
        return Objects.equals(cpf.valor(), associado.cpf.valor());
    }
}
