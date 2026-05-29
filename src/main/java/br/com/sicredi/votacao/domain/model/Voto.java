package br.com.sicredi.votacao.domain.model;

public class Voto {
    private final Associado associado;
    private final VotoValor valor;

    public Voto(Associado associado, VotoValor valor) {
        this.associado = associado;
        this.valor = valor;
    }

    public Associado associado() { return associado; }
    public VotoValor valor() { return valor; }
}