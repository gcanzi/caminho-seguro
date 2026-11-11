package br.com.fiap.caminhoseguro.enums;

/**
 * Tipos de ocorrencia que aumentam o risco de um trecho, cada um com um peso base
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public enum TipoOcorrencia {
    /** Assedio na rua, peso 3 */
    ASSEDIO(3),
    /** Assalto ou roubo, peso 4 */
    ASSALTO(4),
    /** Rua escura relatada pela comunidade, peso 1 */
    RUA_ESCURA(1),
    /** Acidente de transito, peso 3 */
    ACIDENTE(3),
    /** Local deserto, sem movimento de pessoas, peso 2 */
    LOCAL_DESERTO(2);

    private int peso;

    /**
     * Cria o tipo de ocorrencia com o seu peso base
     *
     * @param peso peso base do tipo no calculo de risco
     */
    TipoOcorrencia(int peso) {
        this.peso = peso;
    }

    /**
     * Devolve o peso base do tipo no calculo de risco
     *
     * @return peso base do tipo no calculo de risco
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Devolve o nome do tipo em letras minusculas e sem sublinhado, para usar nos textos
     *
     * @return nome legivel do tipo de ocorrencia
     */
    public String getDescricao() {
        return name().toLowerCase().replace("_", " ");
    }
}
