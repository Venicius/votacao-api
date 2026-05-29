package br.com.sicredi.votacao.domain.model;

public class Pauta {
    private final String id;
    private final String descricao;

    public Pauta(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String id() { return id; }
    public String descricao() { return descricao; }
}