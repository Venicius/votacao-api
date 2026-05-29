package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoEntity, String> {
}