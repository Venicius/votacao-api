package br.com.sicredi.votacao.infra.adapters.in.web.dto;

import java.util.List;

public record FormularioResponse(
        String tipo,
        String titulo,
        List<Campo> itens,
        Botao botaoOk,
        Botao botaoCancelar
) {
    public FormularioResponse(String titulo, List<Campo> items, Botao botaoOk, Botao botaoCancelar) {
        this("FORMULARIO", titulo, items, botaoOk, botaoCancelar);
    }

    public record Campo(
            String tipo,
            String id,
            String titulo,
            Object valor
    ) {
    }

    public record Botao(
            String texto,
            String url,
            Object body
    ) {
    }
}