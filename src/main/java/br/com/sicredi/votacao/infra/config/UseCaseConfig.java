package br.com.sicredi.votacao.infra.config;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoUseCase;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.application.ports.out.ValidadorCpfPort;
import br.com.sicredi.votacao.application.usecase.AbrirSessaoUseCaseImpl;
import br.com.sicredi.votacao.application.usecase.ObterResultadoUseCaseImpl;
import br.com.sicredi.votacao.application.usecase.RegistrarVotoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarVotoUseCase registrarVotoUseCase(
            SessaoRepositoryPort sessaoRepository,
            ValidadorCpfPort validadorCpfPort) {

        return new RegistrarVotoUseCaseImpl(sessaoRepository, validadorCpfPort);
    }

    @Bean
    public AbrirSessaoUseCaseImpl abrirSessaoUseCase(SessaoRepositoryPort repositoryPort) {
        return new AbrirSessaoUseCaseImpl(repositoryPort);
    }

    @Bean
    public ObterResultadoUseCaseImpl obterResultadoUseCase(SessaoRepositoryPort repositoryPort) {
        return new ObterResultadoUseCaseImpl(repositoryPort);
    }
}