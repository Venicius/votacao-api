package br.com.sicredi.votacao.application.ports.in;

import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.VotoValor;

public record RegistrarVotoCommand(
        String sessaoId,
        String associadoId,
        Cpf cpf,
        VotoValor valor
) {}