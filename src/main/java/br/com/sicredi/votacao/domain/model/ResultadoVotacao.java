package br.com.sicredi.votacao.domain.model;

public record ResultadoVotacao(String pautaId, long totalSim, long totalNao) {
    public long totalVotos() {
        return totalSim + totalNao;
    }
}
