package br.com.sicredi.votacao.util;

import java.util.Random;

public class CpfGenerator {

    private static final Random random = new Random();

    public static String gerarCpfValido() {
        int[] digitos = new int[11];

        for (int i = 0; i < 9; i++) {
            digitos[i] = random.nextInt(10);
        }

        digitos[9] = calcularDigito(digitos, 10);

        digitos[10] = calcularDigito(digitos, 11);

        StringBuilder cpf = new StringBuilder();
        for (int digito : digitos) {
            cpf.append(digito);
        }

        return cpf.toString();
    }

    private static int calcularDigito(int[] digitos, int pesoInicial) {
        int soma = 0;
        int limite = pesoInicial - 1;
        int peso = pesoInicial;

        for (int i = 0; i < limite; i++) {
            soma += digitos[i] * peso--;
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}