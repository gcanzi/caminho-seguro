package br.com.fiap.caminhoseguro.enums;

/**
 * Qualidade da iluminacao de um trecho de rua
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public enum Iluminacao {
    /** Iluminacao boa, sem penalidade no risco */
    BOA,
    /** Iluminacao regular, penalidade 1 no risco */
    REGULAR,
    /** Iluminacao ruim, penalidade 3 no risco */
    RUIM
}
