package br.com.sicredi.votacao.application.ports.in;

public interface RegistrarVotoUseCase {
    void executar(RegistrarVotoCommand command);
}