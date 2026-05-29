package br.com.sicredi.votacao.domain.model;

import java.util.Objects;

public class Associado {
    private final String id;
    private final Cpf cpf;

    public Associado(String id, Cpf cpf) {
        this.id = id;
        this.cpf = cpf;
    }

    public String id() { return id; }
    public Cpf cpf() { return cpf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Associado associado = (Associado) o;
        return Objects.equals(id, associado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
