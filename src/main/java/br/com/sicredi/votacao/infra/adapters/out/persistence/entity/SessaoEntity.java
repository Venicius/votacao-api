package br.com.sicredi.votacao.infra.adapters.out.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

    @OneToMany(mappedBy = "sessao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VotoEntity> votos = new HashSet<>();

    public SessaoEntity() {}

    public SessaoEntity(String id, String pautaId, String pautaDescricao, LocalDateTime dataFechamento) {
        this.id = id;
        this.pautaId = pautaId;
        this.pautaDescricao = pautaDescricao;
        this.dataFechamento = dataFechamento;
    }

    public String getId() { return id; }
    public String getPautaId() { return pautaId; }
    public String getPautaDescricao() { return pautaDescricao; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public Set<VotoEntity> getVotos() { return votos; }
}
