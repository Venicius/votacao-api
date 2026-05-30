package br.com.sicredi.votacao.infra.adapters.in.web;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoCommand;
import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.VotoValor;
import br.com.sicredi.votacao.infra.adapters.in.web.dto.VotoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Cpf cpf = new Cpf("12345678901");
        VotoValor valor = VotoValor.valueOf(request.valor().toUpperCase());

        // 2. Monta o Command
        RegistrarVotoCommand command = new RegistrarVotoCommand(sessaoId, cpf, valor);

        // 3. Executa o Caso de Uso
        registrarVotoUseCase.executar(command);

        // 4. Retorna HTTP 202 (Accepted) - Muito comum em sistemas escaláveis (preparando pro Kafka)
        return ResponseEntity.accepted().build();
    }
}