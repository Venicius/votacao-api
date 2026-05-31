package br.com.sicredi.votacao.infra.adapters.out.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cpfClient", url = "${api.cpf.url}")
public interface CpfClient {

    @GetMapping("/{cpf}")
    CpfResponse validarCpf(@PathVariable("cpf") String cpf);

    record CpfResponse(String status) {}
}