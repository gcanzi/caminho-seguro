package br.com.fiap.caminhoseguro.util;

import java.util.Locale;

/**
 * Formata numeros para os textos do app sempre no padrao brasileiro, com virgula decimal
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Formatacao {

    /** Padrao brasileiro, usado para a virgula decimal sair igual em qualquer computador */
    public static final Locale BRASIL = Locale.forLanguageTag("pt-BR");
    /** Quantidade de caracteres da barra de nota */
    public static final int TAMANHO_BARRA = 10;

    private Formatacao() {
    }

    /**
     * Formata um numero com uma casa decimal, como 7,5
     *
     * @param valor numero a ser formatado
     * @return numero com uma casa decimal e virgula
     */
    public static String decimal(double valor) {
        return String.format(BRASIL, "%.1f", valor);
    }

    /**
     * Desenha uma barra de texto proporcional ao valor, como [#######---] para 70 de 100
     *
     * @param valor valor a ser desenhado, de 0 ate o maximo
     * @param maximo valor que enche a barra inteira
     * @return barra com # para a parte cheia e - para a parte vazia
     */
    public static String barra(double valor, double maximo) {
        int cheios = (int) Math.round(valor / maximo * TAMANHO_BARRA);
        String barra = "[";
        for (int i = 0; i < TAMANHO_BARRA; i++) {
            if (i < cheios) {
                barra = barra + "#";
            } else {
                barra = barra + "-";
            }
        }
        return barra + "]";
    }
}
