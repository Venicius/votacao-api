package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoCommand;
import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.VotoValor;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.VotoRequest;
import br.com.sicredi.votacao.util.CpfGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Votos", description = "Endpoints para registro e processamento de votos")
@RestController
@RequestMapping("/v1/sessoes")
public class VotoController {

    private final RegistrarVotoUseCase registrarVotoUseCase;

    public VotoController(RegistrarVotoUseCase registrarVotoUseCase) {
        this.registrarVotoUseCase = registrarVotoUseCase;
    }

    @PostMapping("/{sessaoId}/votos")
    public ResponseEntity<Void> votar(
            @PathVariable String sessaoId,
            @RequestBody VotoRequest request
    ) {

        Cpf cpf = new Cpf(CpfGenerator.gerarCpfValido());
        VotoValor valor = VotoValor.valueOf(request.valor().toUpperCase());

        RegistrarVotoCommand command = new RegistrarVotoCommand(sessaoId, cpf, valor);

        registrarVotoUseCase.executar(command);

        return ResponseEntity.accepted().build();
    }
}