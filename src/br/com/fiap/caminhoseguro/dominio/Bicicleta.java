package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.util.Horario;

/**
 * Perfil de quem pedala: ocorrencias de acidente pesam mais
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Bicicleta implements PerfilDeslocamento {

    /** Acrescimo no risco por acidente no trecho */
    public static final double ACRESCIMO_POR_ACIDENTE = 1.5;

    /**
     * Cria o perfil de bicicleta
     */
    public Bicicleta() {
    }

    /**
     * Devolve o nome do perfil
     *
     * @return "bicicleta"
     */
    @Override
    public String getNome() {
        return "bicicleta";
    }

    /**
     * Soma 1.5 ao risco para cada acidente registrado no trecho
     *
     * @param trecho trecho que esta sendo avaliado
     * @param riscoBase risco do trecho antes do ajuste do perfil
     * @param hora hora do deslocamento, de 0 a 23
     * @return risco ajustado para quem pedala
     */
    @Override
    public double ajustarRisco(Trecho trecho, double riscoBase, int hora) {
        Horario.validarHora(hora);
        int acidentes = trecho.contarOcorrencias(TipoOcorrencia.ACIDENTE);
        return riscoBase + acidentes * ACRESCIMO_POR_ACIDENTE;
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
