package br.com.sicredi.votacao.infra.adapters.out.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sessoes")
public class SessaoEntity {

    @Id
    private String id;

    private String pautaId;
    private String pautaDescricao;
    private LocalDateTime dataFechamento;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "votos",
            joinColumns = @JoinColumn(name = "sessao_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_votos_sessao_cpf", columnNames = {"sessao_id", "cpf"})
    )
    private Set<VotoEntity> votos = new HashSet<>();


    public SessaoEntity() {}

    public SessaoEntity(String id, String pautaId, String pautaDescricao, LocalDateTime dataFechamento, Set<VotoEntity> votos) {
        this.id = id;
        this.pautaId = pautaId;
        this.pautaDescricao = pautaDescricao;
        this.dataFechamento = dataFechamento;
        this.votos = votos;
    }

    public String getId() { return id; }
    public String getPautaId() { return pautaId; }
    public String getPautaDescricao() { return pautaDescricao; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public Set<VotoEntity> getVotos() { return votos; }
}