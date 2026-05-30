package br.com.sicredi.votacao.infra.adapters.in.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotacaoController.class)
class VotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar a tela do tipo SELECAO para votar em uma sessao")
    void deveRetornarTelaSelecaoVoto() throws Exception {
        String sessaoId = "sessao-123";

        mockMvc.perform(get("/v1/sessoes/{id}/votar", sessaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("SELECAO"))
                .andExpect(jsonPath("$.titulo").value("Opções de Voto"))
                .andExpect(jsonPath("$.itens[0].texto").value("Aprovar (SIM)"))
                .andExpect(jsonPath("$.itens[0].url").value("http://api.sicredi.com.br/v1/sessoes/" + sessaoId + "/votos"))
                .andExpect(jsonPath("$.itens[0].body.valor").value("SIM"))

                .andExpect(jsonPath("$.itens[1].texto").value("Rejeitar (NÃO)"))
                .andExpect(jsonPath("$.itens[1].url").value("http://api.sicredi.com.br/v1/sessoes/" + sessaoId + "/votos"))
                .andExpect(jsonPath("$.itens[1].body.valor").value("NAO"));
    }
}