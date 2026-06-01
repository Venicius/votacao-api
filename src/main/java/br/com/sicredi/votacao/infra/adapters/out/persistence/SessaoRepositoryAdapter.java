package br.com.sicredi.votacao.infra.adapters.out.persistence;


import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.domain.model.Associado;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.domain.model.Voto;
import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.SessaoEntity;
import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.VotoEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SessaoRepositoryAdapter implements SessaoRepositoryPort {

    private final SessaoRepository repository;

    public SessaoRepositoryAdapter(SessaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SessaoVotacao> buscarPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public SessaoVotacao salvar(SessaoVotacao sessao) {
        SessaoEntity entity = toEntity(sessao);
        repository.save(entity);
        return sessao;
    }

    @Override
    public boolean existeVotoPorSessaoECpf(String sessaoId, String cpf) {
        return repository.existsBySessaoIdAndCpf(sessaoId, cpf);
    }


    private SessaoEntity toEntity(SessaoVotacao dominio) {
        Set<VotoEntity> votosEntity = dominio.votos().stream()
                .map(v -> new VotoEntity(v.associado().cpf().valor(), v.valor()))
                .collect(Collectors.toSet());

        return new SessaoEntity(
                dominio.id(),
                dominio.pauta().id(),
                dominio.pauta().descricao(),
                dominio.dataFechamento(),
                votosEntity
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