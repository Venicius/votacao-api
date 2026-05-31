package br.com.sicredi.votacao.application.usecase;

import br.com.sicredi.votacao.application.ports.in.AbrirSessaoCommand;
import br.com.sicredi.votacao.application.ports.in.AbrirSessaoUseCase;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AbrirSessaoUseCaseImpl implements AbrirSessaoUseCase {

    private static final Logger log = LoggerFactory.getLogger(AbrirSessaoUseCaseImpl.class);
    private final SessaoRepositoryPort sessaoRepository;

    public AbrirSessaoUseCaseImpl(SessaoRepositoryPort sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Override
    public SessaoVotacao executar(AbrirSessaoCommand command) {

        log.info("Iniciando nova sessão para a pauta: '{}'", command.descricaoPauta());

        String pautaId = UUID.randomUUID().toString();
        String sessaoId = UUID.randomUUID().toString();

        Pauta pauta = new Pauta(pautaId, command.descricaoPauta());

        SessaoVotacao novaSessao = new SessaoVotacao(
                sessaoId,
                pauta,
                command.duracaoMinutos()
        );
        log.info("Sessão criada com sucesso! ID Sessão: {} | Fecha em: {}", novaSessao.id(), novaSessao.dataFechamento());
        return sessaoRepository.salvar(novaSessao);
    }
}