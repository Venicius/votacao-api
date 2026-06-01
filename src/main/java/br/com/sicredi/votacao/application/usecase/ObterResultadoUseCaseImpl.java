package br.com.sicredi.votacao.application.usecase;

import br.com.sicredi.votacao.application.ports.in.ObterResultadoUseCase;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.domain.exception.NotFoundException;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;

public class ObterResultadoUseCaseImpl implements ObterResultadoUseCase {

    private final SessaoRepositoryPort sessaoRepository;

    public ObterResultadoUseCaseImpl(SessaoRepositoryPort sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Override
    public SessaoVotacao executar(String sessaoId) {
        return sessaoRepository.buscarPorId(sessaoId)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada."));
    }
}