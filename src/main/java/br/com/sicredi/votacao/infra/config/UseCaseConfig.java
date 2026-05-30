package br.com.sicredi.votacao.infra.config;

import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.application.usecase.RegistrarVotoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarVotoUseCaseImpl registrarVotoUseCase(SessaoRepositoryPort repositoryPort) {
        return new RegistrarVotoUseCaseImpl(repositoryPort);
    }
}