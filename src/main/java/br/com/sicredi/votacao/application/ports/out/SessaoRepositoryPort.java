package br.com.sicredi.votacao.application.ports.out;

import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import java.util.Optional;

public interface SessaoRepositoryPort {
    Optional<SessaoVotacao> buscarPorId(String id);
    SessaoVotacao salvar(SessaoVotacao sessao);
    boolean existeVotoPorSessaoECpf(String sessaoId, String cpf);
}