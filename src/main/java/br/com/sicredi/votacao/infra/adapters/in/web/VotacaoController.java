package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.infra.adapters.in.web.dto.SelecaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/sessoes")
public class VotacaoController {

    private final String baseApiUrl;
    public VotacaoController(@Value("${api.base-url}") String baseApiUrl) {
        this.baseApiUrl = baseApiUrl;
    }

    @GetMapping("/{id}/votar")
    public ResponseEntity<SelecaoResponse> obterTelaDeVotacao(@PathVariable String id) {

        String urlPostVoto = baseApiUrl + "/sessoes/" + id + "/votos";

        List<SelecaoResponse.ItemSelecao> opcoesDeVoto = List.of(
                new SelecaoResponse.ItemSelecao(
                        "SIM",
                        urlPostVoto,
                        Map.of("valor", "SIM")
                ),
                new SelecaoResponse.ItemSelecao(
                        "NÃO",
                        urlPostVoto,
                        Map.of("valor", "NAO")
                )
        );

        SelecaoResponse telaSelecao = new SelecaoResponse(
                "Opções de Voto",
                opcoesDeVoto
        );

        return ResponseEntity.ok(telaSelecao);
    }
}