package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.Iluminacao;
import br.com.fiap.caminhoseguro.util.Horario;

/**
 * Perfil de quem caminha: a iluminacao ruim a noite pesa mais
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class APe implements PerfilDeslocamento {

    /** Multiplicador do risco a noite em trecho com iluminacao ruim (+30%) */
    public static final double FATOR_NOITE_ILUMINACAO_RUIM = 1.3;
    /** Multiplicador do risco a noite em trecho com iluminacao regular (+10%) */
    public static final double FATOR_NOITE_ILUMINACAO_REGULAR = 1.1;

    /**
     * Cria o perfil a pe
     */
    public APe() {
    }

    /**
     * Devolve o nome do perfil
     *
     * @return "a pe"
     */
    @Override
    public String getNome() {
        return "a pe";
    }

    /**
     * A noite, aumenta o risco em 30% se a iluminacao for ruim e em 10% se for regular
     *
     * @param trecho trecho que esta sendo avaliado
     * @param riscoBase risco do trecho antes do ajuste do perfil
     * @param hora hora do deslocamento, de 0 a 23
     * @return risco ajustado para quem caminha
     */
    @Override
    public double ajustarRisco(Trecho trecho, double riscoBase, int hora) {
        if (Horario.ehNoite(hora)) {
            if (trecho.getIluminacao() == Iluminacao.RUIM) {
                return riscoBase * FATOR_NOITE_ILUMINACAO_RUIM;
            } else if (trecho.getIluminacao() == Iluminacao.REGULAR) {
                return riscoBase * FATOR_NOITE_ILUMINACAO_REGULAR;
            }
        }
        return riscoBase;
    }

    /**
     * Mostra o perfil em uma linha
     *
     * @return nome do perfil
     */
    @Override
    public String toString() {
        return "Perfil " + getNome();
    }
}
