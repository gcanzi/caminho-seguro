package br.com.fiap.caminhoseguro.enums;

/**
 * Qualidade da iluminacao de um trecho de rua, cada uma com a sua penalidade no risco
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public enum Iluminacao {
    /** Iluminacao boa, sem penalidade no risco */
    BOA(0),
    /** Iluminacao regular, penalidade 1 no risco */
    REGULAR(1),
    /** Iluminacao ruim, penalidade 3 no risco */
    RUIM(3);

    private int penalidade;

    /**
     * Cria a iluminacao com a sua penalidade no risco
     *
     * @param penalidade quanto a iluminacao soma no risco do trecho durante o dia
     */
    Iluminacao(int penalidade) {
        this.penalidade = penalidade;
    }

    /**
     * Devolve quanto a iluminacao soma no risco do trecho durante o dia
     *
     * @return penalidade da iluminacao no risco
     */
    public int getPenalidade() {
        return penalidade;
    }
}
