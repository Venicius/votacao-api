package br.com.sicredi.votacao.application.ports.out;

public interface ValidadorCpfPort {
    boolean podeVotar(String cpf);
}
