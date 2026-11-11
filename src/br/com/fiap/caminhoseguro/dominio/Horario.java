package br.com.fiap.caminhoseguro.dominio;

/**
 * Regras de horario usadas no dominio: validar a hora e saber se e noite
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Horario {

    /** Hora em que comeca a noite */
    public static final int INICIO_NOITE = 20;
    /** Hora em que termina a noite */
    public static final int FIM_NOITE = 5;

    private Horario() {
    }

    /**
     * Confere se a hora esta entre 0 e 23
     *
     * @param hora hora a ser verificada, de 0 a 23
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public static void validarHora(int hora) {
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("Hora deve estar entre 0 e 23, recebido: " + hora);
        }
    }

    /**
     * Verifica se a hora esta no periodo da noite, das 20h ate antes das 5h
     *
     * @param hora hora a ser verificada, de 0 a 23
     * @return true se for noite
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public static boolean ehNoite(int hora) {
        validarHora(hora);
        return hora >= INICIO_NOITE || hora < FIM_NOITE;
    }
}
