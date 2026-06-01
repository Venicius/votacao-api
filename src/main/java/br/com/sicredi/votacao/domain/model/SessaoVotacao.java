package br.com.sicredi.votacao.domain.model;

import br.com.sicredi.votacao.domain.exception.DomainBusinessException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SessaoVotacao {
    private final String id;
    private final Pauta pauta;
    private final LocalDateTime dataFechamento;
    private final Set<Voto> votos;

    public SessaoVotacao(String id, Pauta pauta, Integer duracaoMinutos) {
        this.id = id;
        this.pauta = pauta;

        int minutos = (duracaoMinutos != null && duracaoMinutos > 0) ? duracaoMinutos : 1;
        this.dataFechamento = LocalDateTime.now().plusMinutes(minutos);
        this.votos = new HashSet<>();
    }

    public SessaoVotacao(String id, Pauta pauta, LocalDateTime dataFechamento, Set<Voto> votos) {
        this.id = id;
        this.pauta = pauta;
        this.dataFechamento = dataFechamento;
        this.votos = new HashSet<>(votos);
    }

    public void registrarVoto(Voto voto) {
        validarSessaoAberta();

        this.votos.add(voto);
    }

    private void validarSessaoAberta() {
        if (LocalDateTime.now().isAfter(dataFechamento)) {
            throw new DomainBusinessException("A sessão de votação já está encerrada.");
        }
    }

    public ResultadoVotacao contabilizarResultado() {
        long votosSim = votos.stream().filter(v -> v.valor() == VotoValor.SIM).count();
        long votosNao = votos.stream().filter(v -> v.valor() == VotoValor.NAO).count();

        return new ResultadoVotacao(pauta.id(), votosSim, votosNao);
    }

    public String obterStatusDaVotacao() {
        var resultado = contabilizarResultado();

        long sim = resultado.totalSim();
        long nao = resultado.totalNao();

        if (sim > nao) {
            return "APROVADA";
        } else if (nao > sim) {
            return "REJEITADA";
        } else {
            return "EMPATE";
        }
    }

    public String id() {
        return id;
    }

    public Pauta pauta() {
        return pauta;
    }

    public LocalDateTime dataFechamento() {
        return dataFechamento;
    }

    public Set<Voto> votos() {
        return Collections.unmodifiableSet(votos);
    }

    public boolean isAberta() {
        return LocalDateTime.now().isBefore(dataFechamento) || LocalDateTime.now().isEqual(dataFechamento);
    }
}