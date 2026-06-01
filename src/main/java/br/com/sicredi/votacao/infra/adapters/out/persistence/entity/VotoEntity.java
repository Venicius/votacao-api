package br.com.sicredi.votacao.infra.adapters.out.persistence.entity;

import br.com.sicredi.votacao.domain.model.VotoValor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "votos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_votos_sessao_cpf", columnNames = {"sessao_id", "cpf"})
})
public class VotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoEntity sessao;

    private String cpf;

    @Enumerated(EnumType.STRING)
    private VotoValor valor;

    public VotoEntity() {}

    public VotoEntity(SessaoEntity sessao, String cpf, VotoValor valor) {
        this.sessao = sessao;
        this.cpf = cpf;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public String getCpf() { return cpf; }
    public VotoValor getValor() { return valor; }
}
