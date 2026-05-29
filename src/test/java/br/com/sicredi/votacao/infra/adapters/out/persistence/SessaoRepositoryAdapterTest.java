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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SessaoRepositoryAdapterTest {

    @Autowired
    private SessaoRepository springDataRepository;

    @Test
    @DisplayName("Deve salvar uma sessao de votacao e recuperar")
    void deveSalvarERecuperarSessao() {

        SessaoRepositoryAdapter adapter = new SessaoRepositoryAdapter(springDataRepository);

        Pauta pauta = new Pauta("pauta-1", "Aprovação de emprestimo");
        SessaoVotacao sessao = new SessaoVotacao("sessao-1", pauta, 10);
        sessao.registrarVoto(new Voto(new Associado("assoc-1", new Cpf("12345678901")), VotoValor.SIM));

        adapter.salvar(sessao);
        Optional<SessaoVotacao> sessaoRecuperada = adapter.buscarPorId("sessao-1");


        assertTrue(sessaoRecuperada.isPresent());
        SessaoVotacao sessaoSalva = sessaoRecuperada.get();
        assertEquals("pauta-1", sessaoSalva.pauta().id());
        assertEquals("Aprovação de emprestimo", sessaoSalva.pauta().descricao());
        assertEquals(1, sessaoSalva.votos().size());
        assertEquals(VotoValor.SIM, sessaoSalva.votos().iterator().next().valor());
    }
}