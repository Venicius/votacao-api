package br.com.sicredi.votacao.infra.adapters.out.persistence.entity;

import br.com.sicredi.votacao.domain.model.VotoValor;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class VotoEntity {
    private String associadoId;
    private String cpf;

    @Enumerated(EnumType.STRING)
    private VotoValor valor;

    public VotoEntity() {}

    public VotoEntity(String associadoId, String cpf, VotoValor valor) {
        this.associadoId = associadoId;
        this.cpf = cpf;
        this.valor = valor;
    }

    public String getAssociadoId() { return associadoId; }
    public String getCpf() { return cpf; }
    public VotoValor getValor() { return valor; }
}