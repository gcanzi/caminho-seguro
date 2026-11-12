package br.com.fiap.caminhoseguro.enums;

/**
 * Nivel de risco de um trecho, para mostrar o risco de 0 a 10 de um jeito facil de entender
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public enum NivelRisco {
    /** Risco abaixo de 3 */
    BAIXO,
    /** Risco de 3 ate abaixo de 6 */
    MEDIO,
    /** Risco de 6 ate 10 */
    ALTO;

    /** Risco a partir do qual o nivel deixa de ser baixo */
    public static final double LIMITE_MEDIO = 3.0;
    /** Risco a partir do qual o nivel passa a ser alto */
    public static final double LIMITE_ALTO = 6.0;

    /**
     * Classifica um risco de 0 a 10 em baixo, medio ou alto
     *
     * @param risco risco do trecho, de 0 a 10
     * @return nivel correspondente ao risco
     */
    public static NivelRisco classificar(double risco) {
        if (risco < LIMITE_MEDIO) {
            return BAIXO;
        } else if (risco < LIMITE_ALTO) {
            return MEDIO;
        } else {
            return ALTO;
        }
    }

    /**
     * Devolve o nome do nivel em letras minusculas, para usar nos textos
     *
     * @return nome legivel do nivel de risco
     */
    public String getDescricao() {
        return name().toLowerCase();
    }
}
