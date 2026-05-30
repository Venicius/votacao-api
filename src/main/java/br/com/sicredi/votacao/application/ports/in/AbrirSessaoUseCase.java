package br.com.sicredi.votacao.application.ports.in;

import br.com.sicredi.votacao.domain.model.SessaoVotacao;

public interface AbrirSessaoUseCase {
    SessaoVotacao executar(AbrirSessaoCommand command);
}