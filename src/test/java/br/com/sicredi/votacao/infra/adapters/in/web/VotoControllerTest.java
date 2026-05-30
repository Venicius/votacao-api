package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotoController.class)
@TestPropertySource(properties = "api.base-url=http://api.teste.local/v1")
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrarVotoUseCase registrarVotoUseCase;

    @Value("${api.base-url}")
    private String baseApiUrl;

    @Test
    @DisplayName("Deve receber a requisição de voto e retornar 202 Accepted")
    void deveReceberVotoComSucesso() throws Exception {

        String jsonBody = """
                {
                    "valor": "SIM"
                }
                """;

        mockMvc.perform(post(baseApiUrl + "/sessoes/sessao-123/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isAccepted());
    }
}