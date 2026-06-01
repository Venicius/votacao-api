package br.com.sicredi.votacao.application.ports.out;

import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.domain.model.Voto;

import java.util.Optional;

public interface SessaoRepositoryPort {
    Optional<SessaoVotacao> buscarPorId(String id);
    SessaoVotacao salvar(SessaoVotacao sessao);
    void adicionarVoto(String sessaoId, Voto voto);
}