package br.com.sicredi.votacao.infra.adapters.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.URI;

@FeignClient(name = "cpfClient",url = "http://localhost")
public interface CpfClient {

    @GetMapping("/{cpf}")
    CpfResponse validarCpf(URI baseUri, @PathVariable("cpf") String cpf);

    record CpfResponse(String status) {}
}