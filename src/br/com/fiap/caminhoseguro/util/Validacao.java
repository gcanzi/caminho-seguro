package br.com.fiap.caminhoseguro.util;

/**
 * Validacoes reutilizadas pelos setters e servicos; todas lancam IllegalArgumentException
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Validacao {

    private Validacao() {
    }

    /**
     * Confere se um valor obrigatorio foi informado
     *
     * @param valor valor a ser verificado
     * @param mensagem mensagem do erro caso o valor seja nulo
     *
     * @throws IllegalArgumentException se o valor for nulo
     */
    public static void obrigatorio(Object valor, String mensagem) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    /**
     * Confere se um texto obrigatorio foi preenchido
     *
     * @param valor texto a ser verificado
     * @param mensagem mensagem do erro caso o texto seja nulo ou vazio
     *
     * @throws IllegalArgumentException se o texto for nulo ou so tiver espacos
     */
    public static void textoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    /**
     * Confere se um numero inteiro esta dentro de uma faixa
     *
     * @param valor numero a ser verificado
     * @param minimo menor valor aceito
     * @param maximo maior valor aceito
     * @param campo nome do campo usado na mensagem de erro
     *
     * @throws IllegalArgumentException se o valor estiver fora da faixa
     */
    public static void faixa(int valor, int minimo, int maximo, String campo) {
        if (valor < minimo || valor > maximo) {
            throw new IllegalArgumentException(campo + " deve estar entre " + minimo + " e " + maximo
                    + ", recebido: " + valor);
        }
    }

    /**
     * Confere se um numero decimal esta dentro de uma faixa
     *
     * @param valor numero a ser verificado
     * @param minimo menor valor aceito
     * @param maximo maior valor aceito
     * @param campo nome do campo usado na mensagem de erro
     *
     * @throws IllegalArgumentException se o valor estiver fora da faixa
     */
    public static void faixa(double valor, int minimo, int maximo, String campo) {
        if (valor < minimo || valor > maximo) {
            throw new IllegalArgumentException(campo + " deve estar entre " + minimo + " e " + maximo
                    + ", recebido: " + valor);
        }
    }
}
