package br.com.sicredi.votacao.infra.adapters.in.web.dto;

public record NovaSessaoRequest(
        String descricaoPauta,
        Integer duracaoMinutos
) {}