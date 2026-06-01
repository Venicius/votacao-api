package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.infra.adapters.out.persistence.entity.SessaoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SessaoEntity s WHERE s.id = :id")
    Optional<SessaoEntity> findByIdForUpdate(@Param("id") String id);
}