package br.com.sicredi.votacao.infra.adapters.out.client;

import br.com.sicredi.votacao.application.ports.out.ValidadorCpfPort;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCpfAdapter implements ValidadorCpfPort {
    private static final Logger log = LoggerFactory.getLogger(ValidadorCpfAdapter.class);
    private final CpfClient cpfClient;

    public ValidadorCpfAdapter(CpfClient cpfClient) {
        this.cpfClient = cpfClient;
    }

    @Override
    public boolean podeVotar(String cpf) {
        try {
            CpfClient.CpfResponse response = cpfClient.validarCpf(cpf);
            return "ABLE_TO_VOTE".equalsIgnoreCase(response.status());
        } catch (FeignException.NotFound e) {
            log.warn("CPF {} não encontrado na API externa.", cpf);
            return false;
        } catch (Exception e) {
            log.error("Erro ao validar CPF na API externa", e);
            return false;
        }
    }
}
