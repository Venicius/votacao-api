package br.com.sicredi.votacao.infra.adapters.out.client;

import br.com.sicredi.votacao.application.ports.out.ValidadorCpfPort;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ValidadorCpfAdapter implements ValidadorCpfPort {
    private static final Logger log = LoggerFactory.getLogger(ValidadorCpfAdapter.class);
    private final CpfClient cpfClient;
    private final URI realUri;
    private final URI mockUri;

    public ValidadorCpfAdapter(
            CpfClient cpfClient,
            @Value("${api.cpf.real-url}") String realUrl,
            @Value("${api.cpf.mock-url}") String mockUrl) {
        this.cpfClient = cpfClient;
        this.realUri = URI.create(realUrl);
        this.mockUri = URI.create(mockUrl);
    }

    @Override
    public boolean podeVotar(String cpf) {
        try {
            log.debug("Tentando validar CPF {} na API disponibilizada", cpf);
            CpfClient.CpfResponse response = cpfClient.validarCpf(realUri, cpf);
            return "ABLE_TO_VOTE".equalsIgnoreCase(response.status());

        } catch (Exception e) {
            log.warn("API falhou ou deu timeout para o CPF {}. Acionando Fallback (WireMock)", cpf);
            try {
                CpfClient.CpfResponse mockResponse = cpfClient.validarCpf(mockUri, cpf);
                return "ABLE_TO_VOTE".equalsIgnoreCase(mockResponse.status());

            } catch (Exception mockEx) {
                log.error("Fallback também falhou para o CPF {}", cpf, mockEx);
                return false;
            }
        }
    }
}
