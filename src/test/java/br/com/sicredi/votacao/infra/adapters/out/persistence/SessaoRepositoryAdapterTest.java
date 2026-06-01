package br.com.sicredi.votacao.infra.adapters.out.persistence;

import br.com.sicredi.votacao.domain.model.Associado;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.domain.model.Voto;
import br.com.sicredi.votacao.domain.model.VotoValor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SessaoRepositoryAdapterTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoJpaRepository votoJpaRepository;

    @Test
    @DisplayName("Deve salvar sessão e recuperar com votos adicionados via adicionarVoto")
    void deveSalvarERecuperarSessaoComVoto() {
        SessaoRepositoryAdapter adapter = new SessaoRepositoryAdapter(sessaoRepository, votoJpaRepository);

        Pauta pauta = new Pauta("pauta-1", "Aprovação de emprestimo");
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", pauta, 10);
        adapter.salvar(sessao);

        adapter.adicionarVoto("sessao-1", new Voto(new Associado(new Cpf("12345678901")), VotoValor.SIM));

        entityManager.flush();
        entityManager.clear();

        Optional<SessaoVotacao> sessaoRecuperada = adapter.buscarPorId("sessao-1");

        assertTrue(sessaoRecuperada.isPresent());
        SessaoVotacao sessaoSalva = sessaoRecuperada.get();
        assertEquals("pauta-1", sessaoSalva.pauta().id());
        assertEquals("Aprovação de emprestimo", sessaoSalva.pauta().descricao());
        assertEquals(1, sessaoSalva.votos().size());
        assertEquals(VotoValor.SIM, sessaoSalva.votos().iterator().next().valor());
    }
}
