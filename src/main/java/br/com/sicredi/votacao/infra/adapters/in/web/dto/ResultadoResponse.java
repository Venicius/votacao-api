package br.com.sicredi.votacao.infra.adapters.in.web.dto;

public record ResultadoResponse(
        String sessaoId,
        String pauta,
        long totalSim,
        long totalNao,
        String status
) {
}