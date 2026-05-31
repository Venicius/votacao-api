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
        Associado associado = new Associado(new Cpf("12345678901"));
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
        Associado associado = new Associado(new Cpf("12345678901"));

        Voto voto1 = new Voto(associado, VotoValor.SIM);
        Voto voto2 = new Voto(associado, VotoValor.NAO);

        sessao.registrarVoto(voto1);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class, () -> {
            sessao.registrarVoto(voto2);
        });

        assertEquals("Associado já registrou um voto nesta pauta.", exception.getMessage());
        assertEquals(1, sessao.votos().size());
    }

    @Test
    @DisplayName("Deve retornar status APROVADA quando tiver mais votos SIM")
    void deveRetornarAprovada() {
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", new Pauta("pauta-1", "Teste"), 10);

        sessao.registrarVoto(new Voto(new Associado(new Cpf("11111111111")), VotoValor.SIM));
        sessao.registrarVoto(new Voto(new Associado(new Cpf("22222222222")), VotoValor.SIM));
        sessao.registrarVoto(new Voto(new Associado(new Cpf("33333333333")), VotoValor.NAO));

        assertEquals("APROVADA", sessao.obterStatusDaVotacao());
    }

    @Test
    @DisplayName("Deve retornar status REJEITADA quando tiver mais votos NAO")
    void deveRetornarRejeitada() {
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", new Pauta("pauta-1", "Teste"), 10);

        sessao.registrarVoto(new Voto(new Associado(new Cpf("11111111111")), VotoValor.NAO));
        sessao.registrarVoto(new Voto(new Associado(new Cpf("22222222222")), VotoValor.NAO));

        assertEquals("REJEITADA", sessao.obterStatusDaVotacao());
    }

    @Test
    @DisplayName("Deve retornar status EMPATE quando os votos forem iguais")
    void deveRetornarEmpate() {
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", new Pauta("pauta-1", "Teste"), 10);

        sessao.registrarVoto(new Voto(new Associado(new Cpf("11111111111")), VotoValor.SIM));
        sessao.registrarVoto(new Voto(new Associado(new Cpf("22222222222")), VotoValor.NAO));

        assertEquals("EMPATE", sessao.obterStatusDaVotacao());
    }

    @Test
    @DisplayName("Deve lançar excepção ao tentar registar voto em uma sessão expirada")
    void deveLancarExcecaoQuandoSessaoExpirada() throws Exception {

        SessaoVotacao sessaoExpirada = new SessaoVotacao("sessao-1", new Pauta("pauta-1", "Teste"), 10);

        java.lang.reflect.Field campoData = SessaoVotacao.class.getDeclaredField("dataFechamento");
        campoData.setAccessible(true);
        campoData.set(sessaoExpirada, java.time.LocalDateTime.now().minusMinutes(5));

        Voto votoAtrasado = new Voto(new Associado(new Cpf("11111111111")), VotoValor.SIM);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class,
                () -> sessaoExpirada.registrarVoto(votoAtrasado));

        assertEquals("A sessão de votação já está encerrada.", exception.getMessage());
    }
}