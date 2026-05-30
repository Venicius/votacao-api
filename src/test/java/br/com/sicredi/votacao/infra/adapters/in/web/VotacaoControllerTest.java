package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.application.ports.in.AbrirSessaoUseCase;
import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import br.com.sicredi.votacao.domain.model.Pauta;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotacaoController.class)
@TestPropertySource(properties = "api.base-url=http://api.teste.local/v1")
class VotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrarVotoUseCase registrarVotoUseCase;

    @MockBean
    private AbrirSessaoUseCase abrirSessaoUseCase;

    @Value("${api.base-url}")
    private String baseApiUrl;

    @Test
    @DisplayName("Deve retornar a tela do tipo SELECAO para votar em uma sessao")
    void deveRetornarTelaSelecaoVoto() throws Exception {
        String sessaoId = "sessao-123";

        mockMvc.perform(get("/v1/sessoes/{id}/votar", sessaoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("SELECAO"))
                .andExpect(jsonPath("$.titulo").value("Opções de Voto"))
                .andExpect(jsonPath("$.itens[0].texto").value("SIM"))
                .andExpect(jsonPath("$.itens[0].url").value(baseApiUrl + "/sessoes/" + sessaoId + "/votos"))
                .andExpect(jsonPath("$.itens[0].body.valor").value("SIM"))

                .andExpect(jsonPath("$.itens[1].texto").value("NÃO"))
                .andExpect(jsonPath("$.itens[1].url").value(baseApiUrl + "/sessoes/" + sessaoId + "/votos"))
                .andExpect(jsonPath("$.itens[1].body.valor").value("NAO"));
    }

    @Test
    @DisplayName("Deve receber requisição para abrir nova sessão e retornar 201 Created")
    void deveAbrirNovaSessao() throws Exception {

        String jsonBody = """
                {
                    "descricaoPauta": "Aprovação do Balanço 2025",
                    "duracaoMinutos": 10
                }
                """;

        SessaoVotacao sessaoMock = new SessaoVotacao(
                "sessao-123",
                new Pauta("pauta-123", "Aprovação do Balanço 2025"),
                10
        );

        when(abrirSessaoUseCase.executar(any())).thenReturn(sessaoMock);


        mockMvc.perform(post(baseApiUrl + "/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",  baseApiUrl +"/sessoes/sessao-123"));
    }

}