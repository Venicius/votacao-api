package br.com.sicredi.votacao.application.ports.in;

public record AbrirSessaoCommand(
        String descricaoPauta,
        Integer duracaoMinutos
) {}
