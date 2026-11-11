package br.com.fiap.caminhoseguro.dominio;

/**
 * Forma como a pessoa se desloca; cada perfil ajusta o risco de um trecho do seu jeito
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public interface PerfilDeslocamento {

    /**
     * Devolve o nome do perfil
     *
     * @return nome do perfil, por exemplo "a pe"
     */
    String getNome();

    /**
     * Ajusta o risco base do trecho de acordo com o perfil e o horario
     *
     * @param trecho trecho que esta sendo avaliado
     * @param riscoBase risco do trecho antes do ajuste do perfil
     * @param hora hora do deslocamento, de 0 a 23
     * @return risco ajustado para este perfil
     */
    double ajustarRisco(Trecho trecho, double riscoBase, int hora);
}
