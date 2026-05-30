package br.com.sicredi.votacao.infra.adapters.in.web.dto;

import java.util.List;

public record FormularioResponse(
        String tipo,
        String titulo,
        String urlPost,
        List<CampoFormulario> campos
) {
    public FormularioResponse(String titulo, String urlPost, List<CampoFormulario> campos) {
        this("FORMULARIO", titulo, urlPost, campos);
    }

    public record CampoFormulario(
            String id,
            String tipo,
            String label,
            String dica
    ) {
    }
}