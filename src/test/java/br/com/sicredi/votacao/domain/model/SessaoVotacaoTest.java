package br.com.sicredi.votacao.domain.model;

import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessaoVotacaoTest {

    @Test
    @DisplayName("Deve inicializar sessão com 1 minuto por default quando tempo for nulo")
    void deveInicializarSessaoComTempoDefault() {
        Pauta pauta = new Pauta("pauta-1", "Nova Pauta");
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", pauta, null);

        assertTrue(sessao.isAberta());
        assertNotNull(sessao.dataFechamento());
    }

    @Test
    @DisplayName("Deve registrar voto com sucesso")
    void deveRegistrarVotoComSucesso() {
        Pauta pauta = new Pauta("pauta-1", "Nova Pauta");
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", pauta, 5);
        Associado associado = new Associado("assoc-1", new Cpf("12345678901"));
        Voto voto = new Voto(associado, VotoValor.SIM);

        sessao.registrarVoto(voto);

        assertEquals(pauta.descricao(), sessao.pauta().descricao());
        assertEquals(1, sessao.votos().size());
        assertEquals(1, sessao.contabilizarResultado().totalSim());

    }

    @Test
    @DisplayName("Não deve permitir que o mesmo associado vote duas vezes na mesma pauta")
    void naoDevePermitirVotoDuplicado() {
        Pauta pauta = new Pauta("pauta-1", "Nova Pauta");
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", pauta, 5);
        Associado associado = new Associado("assoc-1", new Cpf("12345678901"));

        Voto voto1 = new Voto(associado, VotoValor.SIM);
        Voto voto2 = new Voto(associado, VotoValor.NAO);

        sessao.registrarVoto(voto1);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class, () -> {
            sessao.registrarVoto(voto2);
        });

        assertEquals("Associado já registrou um voto nesta pauta.", exception.getMessage());
        assertEquals(1, sessao.votos().size());
    }
}