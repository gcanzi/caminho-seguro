package br.com.fiap.caminhoseguro.dominio;

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
}
