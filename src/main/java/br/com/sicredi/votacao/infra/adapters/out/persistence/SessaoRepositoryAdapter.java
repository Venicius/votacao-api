package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import br.com.sicredi.votacao.domain.exception.NotFoundException;
import br.com.sicredi.votacao.domain.model.Associado;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.domain.model.Voto;
import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.SessaoEntity;
import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.VotoEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SessaoRepositoryAdapter implements SessaoRepositoryPort {

    private final SessaoRepository repository;
    private final VotoJpaRepository votoRepository;

    public SessaoRepositoryAdapter(SessaoRepository repository, VotoJpaRepository votoRepository) {
        this.repository = repository;
        this.votoRepository = votoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessaoVotacao> buscarPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public SessaoVotacao salvar(SessaoVotacao sessao) {
        repository.save(toEntity(sessao));
        return sessao;
    }

    @Override
    @Transactional
    public void adicionarVoto(String sessaoId, Voto voto) {
        SessaoEntity sessao = repository.findByIdForUpdate(sessaoId)
                .orElseThrow(() -> new NotFoundException("Sessão de votação não encontrada."));

        if (LocalDateTime.now().isAfter(sessao.getDataFechamento())) {
            throw new DomainBusinessException("A sessão de votação já está encerrada.");
        }

        String cpf = voto.associado().cpf().valor();
        if (votoRepository.existsBySessaoIdAndCpf(sessaoId, cpf)) {
            throw new DomainBusinessException("Associado já registrou um voto nesta pauta.");
        }

        votoRepository.save(new VotoEntity(sessao, cpf, voto.valor()));
    }

    private SessaoEntity toEntity(SessaoVotacao dominio) {
        return new SessaoEntity(
                dominio.id(),
                dominio.pauta().id(),
                dominio.pauta().descricao(),
                dominio.dataFechamento()
        );
    }

    private SessaoVotacao toDomain(SessaoEntity entity) {
        Pauta pauta = new Pauta(entity.getPautaId(), entity.getPautaDescricao());

        Set<Voto> votosDomain = entity.getVotos().stream()
                .map(v -> new Voto(new Associado(new Cpf(v.getCpf())), v.getValor()))
                .collect(Collectors.toSet());

        return new SessaoVotacao(entity.getId(), pauta, entity.getDataFechamento(), votosDomain);
    }
}
