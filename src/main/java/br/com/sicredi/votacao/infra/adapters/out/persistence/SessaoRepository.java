package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoEntity, String> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM votos WHERE sessao_id = :sessaoId AND cpf = :cpf)", nativeQuery = true)
    boolean existsBySessaoIdAndCpf(@Param("sessaoId") String sessaoId, @Param("cpf") String cpf);
}