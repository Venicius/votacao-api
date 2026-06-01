package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.VotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoJpaRepository extends JpaRepository<VotoEntity, Long> {
    boolean existsBySessaoIdAndCpf(String sessaoId, String cpf);
}
