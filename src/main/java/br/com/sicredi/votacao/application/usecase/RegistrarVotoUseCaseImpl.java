package br.com.sicredi.votacao.application.usecase;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoCommand;
import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.application.ports.out.ValidadorCpfPort;
import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import br.com.sicredi.votacao.domain.model.Associado;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.domain.model.Voto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrarVotoUseCaseImpl implements RegistrarVotoUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarVotoUseCaseImpl.class);
    private final SessaoRepositoryPort sessaoRepository;
    private final ValidadorCpfPort validadorCpfPort;

    public RegistrarVotoUseCaseImpl(SessaoRepositoryPort sessaoRepository, ValidadorCpfPort validadorCpfPort) {
        this.sessaoRepository = sessaoRepository;
        this.validadorCpfPort = validadorCpfPort;
    }

    @Override
    public void executar(RegistrarVotoCommand command) {
        log.debug("Processando intenção de voto na Sessão {}",  command.sessaoId());
        if (!validadorCpfPort.podeVotar(command.cpf().valor())) {
            throw new DomainBusinessException("Associado não está apto para votar (Validação CPF).");
        }
        SessaoVotacao sessao = sessaoRepository.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new DomainBusinessException("Sessão de votação não encontrada."));

        Associado associado = new Associado(command.cpf());
        Voto voto = new Voto(associado, command.valor());

        sessao.registrarVoto(voto);
        log.info("Voto registado com sucesso para a Sessão {}", command.sessaoId());
        sessaoRepository.salvar(sessao);
    }
}