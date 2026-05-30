package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.application.ports.in.AbrirSessaoCommand;
import br.com.sicredi.votacao.application.ports.in.AbrirSessaoUseCase;
import br.com.sicredi.votacao.application.ports.in.ObterResultadoUseCase;
import br.com.sicredi.votacao.domain.model.SessaoVotacao;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.FormularioResponse;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.NovaSessaoRequest;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.ResultadoResponse;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.SelecaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/sessoes")
public class VotacaoController {

    private final String baseApiUrl;
    private final AbrirSessaoUseCase abrirSessaoUseCase;
    private final ObterResultadoUseCase obterResultadoUseCase;

    public VotacaoController(
            @Value("${api.base-url}") String baseApiUrl,
            AbrirSessaoUseCase abrirSessaoUseCase,
            ObterResultadoUseCase obterResultadoUseCase
    ) {
        this.baseApiUrl = baseApiUrl;
        this.abrirSessaoUseCase = abrirSessaoUseCase;
        this.obterResultadoUseCase = obterResultadoUseCase;
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

    @GetMapping("/nova")
    public ResponseEntity<FormularioResponse> obterTelaNovaSessao() {

        String urlPostSessao = baseApiUrl + "/sessoes";

        List<FormularioResponse.Campo> campos = List.of(
                new FormularioResponse.Campo("INPUT_TEXTO", "descricaoPauta", "Descrição da Pauta", ""),
                new FormularioResponse.Campo("INPUT_NUMERO", "duracaoMinutos", "Duração (em minutos)", 1)
        );

        FormularioResponse.Botao botaoOk = new FormularioResponse.Botao(
                "Abrir Sessão",
                urlPostSessao,
                Map.of()
        );

        FormularioResponse.Botao botaoCancelar = new FormularioResponse.Botao(
                "Cancelar",
                "http://sicredi.com.br/home",
                null
        );

        FormularioResponse telaFormulario = new FormularioResponse(
                "Cadastrar Nova Pauta",
                campos,
                botaoOk,
                botaoCancelar
        );

        return ResponseEntity.ok(telaFormulario);
    }

    @PostMapping
    public ResponseEntity<Void> abrirSessao(@RequestBody NovaSessaoRequest request) {

        AbrirSessaoCommand command = new AbrirSessaoCommand(request.descricaoPauta(), request.duracaoMinutos());

        SessaoVotacao sessao = abrirSessaoUseCase.executar(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sessao.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}/resultado")
    public ResponseEntity<ResultadoResponse> obterResultado(@PathVariable String id) {

        SessaoVotacao sessao = obterResultadoUseCase.executar(id);

        ResultadoResponse response = new ResultadoResponse(
                sessao.id(),
                sessao.pauta().descricao(),
                sessao.contabilizarResultado().totalSim(),
                sessao.contabilizarResultado().totalNao(),
                sessao.obterStatusDaVotacao()
        );

        return ResponseEntity.ok(response);
    }
}