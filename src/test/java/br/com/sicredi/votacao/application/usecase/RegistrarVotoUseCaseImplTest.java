package br.com.sicredi.votacao.application.usecase;

import br.com.sicredi.votacao.application.ports.in.RegistrarVotoCommand;
import br.com.sicredi.votacao.application.ports.out.SessaoRepositoryPort;
import br.com.sicredi.votacao.application.ports.out.ValidadorCpfPort;
import br.com.sicredi.votacao.domain.exception.DomainBusinessException;
import br.com.sicredi.votacao.domain.model.Cpf;
import br.com.sicredi.votacao.domain.model.Voto;
import br.com.sicredi.votacao.domain.model.VotoValor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarVotoUseCaseImplTest {

    @Mock
    private SessaoRepositoryPort sessaoRepository;

    @Mock
    private ValidadorCpfPort validadorCpfPort;

    @InjectMocks
    private RegistrarVotoUseCaseImpl registrarVotoUseCase;

    @Test
    @DisplayName("Deve registar o voto quando o CPF for apto")
    void deveRegistrarVotoQuandoCpfApto() {
        RegistrarVotoCommand command = new RegistrarVotoCommand("sessao-1", new Cpf("12345678901"), VotoValor.SIM);

        when(validadorCpfPort.podeVotar("12345678901")).thenReturn(true);

        assertDoesNotThrow(() -> registrarVotoUseCase.executar(command));

        verify(sessaoRepository, times(1)).adicionarVoto(anyString(), any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar excepção quando o CPF NÃO for apto para votar")
    void deveLancarExcecaoQuandoCpfInapto() {
        RegistrarVotoCommand command = new RegistrarVotoCommand("sessao-1", new Cpf("12345678901"), VotoValor.SIM);

        when(validadorCpfPort.podeVotar("12345678901")).thenReturn(false);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class,
                () -> registrarVotoUseCase.executar(command));

        assertEquals("Associado não está apto para votar (Validação CPF).", exception.getMessage());

        verify(sessaoRepository, never()).adicionarVoto(anyString(), any());
    }

    @Test
    @DisplayName("Deve propagar exceção quando o associado já tiver votado na pauta")
    void devePropgarExcecaoQuandoAssociadoJaVotou() {
        RegistrarVotoCommand command = new RegistrarVotoCommand("sessao-1", new Cpf("12345678901"), VotoValor.SIM);

        when(validadorCpfPort.podeVotar("12345678901")).thenReturn(true);
        doThrow(new DomainBusinessException("Associado já registrou um voto nesta pauta."))
                .when(sessaoRepository).adicionarVoto(anyString(), any(Voto.class));

        DomainBusinessException exception = assertThrows(DomainBusinessException.class,
                () -> registrarVotoUseCase.executar(command));

        assertEquals("Associado já registrou um voto nesta pauta.", exception.getMessage());
    }
}
