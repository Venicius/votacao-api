package br.com.sicredi.votacao.application.usecase;

import br.com.sicredi.votacao.application.ports.in.AbrirSessaoCommand;
import br.com.sicredi.votacao.application.ports.in.AbrirSessaoUseCase;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;

import java.util.UUID;

public class AbrirSessaoUseCaseImpl implements AbrirSessaoUseCase {

    private final SessaoRepositoryPort sessaoRepository;

    public AbrirSessaoUseCaseImpl(SessaoRepositoryPort sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Override
    public SessaoVotacao executar(AbrirSessaoCommand command) {

        String pautaId = UUID.randomUUID().toString();
        String sessaoId = UUID.randomUUID().toString();

        Pauta pauta = new Pauta(pautaId, command.descricaoPauta());

        SessaoVotacao novaSessao = new SessaoVotacao(
                sessaoId,
                pauta,
                command.duracaoMinutos()
        );

        return sessaoRepository.salvar(novaSessao);
    }
}