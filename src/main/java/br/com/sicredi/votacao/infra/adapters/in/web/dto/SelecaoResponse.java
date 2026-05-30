package br.com.sicredi.votacao.infra.adapters.in.web.dto;

import java.util.List;

public record SelecaoResponse(
        String tipo,
        String titulo,
        List<ItemSelecao> itens
) {
    public SelecaoResponse(String titulo, List<ItemSelecao> itens) {
        this("SELECAO", titulo, itens);
    }

    public record ItemSelecao(
            String texto,
            String url,
            Object body
    ) {}
}
