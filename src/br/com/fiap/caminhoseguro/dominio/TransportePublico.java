package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.util.Horario;

/**
 * Perfil de quem usa onibus ou metro: so caminha ate o ponto, mas local deserto a noite pesa mais
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class TransportePublico implements PerfilDeslocamento {

    /** Reducao do risco para quem so caminha ate o ponto */
    public static final double FATOR_CAMINHADA = 0.8;
    /** Acrescimo no risco por local deserto a noite */
    public static final double ACRESCIMO_DESERTO_NOITE = 1.5;

    /**
     * Cria o perfil de transporte publico
     */
    public TransportePublico() {
    }

    /**
     * Devolve o nome do perfil
     *
     * @return "transporte publico"
     */
    @Override
    public String getNome() {
        return "transporte publico";
    }

    /**
     * Reduz o risco em 20% e, a noite, soma 1.5 para cada local deserto no trecho
     *
     * @param trecho trecho que esta sendo avaliado
     * @param riscoBase risco do trecho antes do ajuste do perfil
     * @param hora hora do deslocamento, de 0 a 23
     * @return risco ajustado para quem usa transporte publico
     */
    @Override
    public double ajustarRisco(Trecho trecho, double riscoBase, int hora) {
        double risco = riscoBase * FATOR_CAMINHADA;
        if (Horario.ehNoite(hora)) {
            int desertos = trecho.contarOcorrencias(TipoOcorrencia.LOCAL_DESERTO);
            risco = risco + desertos * ACRESCIMO_DESERTO_NOITE;
        }
        return risco;
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
