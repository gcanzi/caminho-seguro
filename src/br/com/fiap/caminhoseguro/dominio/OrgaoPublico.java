package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoPontoApoio;

/**
 * Orgao publico que serve de Ponto Seguro, como delegacia, base da GCM, hospital ou UBS
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class OrgaoPublico extends PontoApoio {

    /**
     * Cria um orgao publico
     *
     * @param id identificador do ponto de apoio
     * @param nome nome do ponto de apoio
     * @param tipo tipo do ponto de apoio
     * @param posicaoX coordenada X do ponto no plano do bairro, em metros
     * @param posicaoY coordenada Y do ponto no plano do bairro, em metros
     * @param horaAbertura hora de abertura, de 0 a 23
     * @param horaFechamento hora de fechamento, de 0 a 23
     *
     * @throws IllegalArgumentException se o tipo for COMERCIO ou algum dado for invalido
     */
    public OrgaoPublico(int id, String nome, TipoPontoApoio tipo, double posicaoX, double posicaoY,
                        int horaAbertura, int horaFechamento) {
        super(id, nome, tipo, posicaoX, posicaoY, horaAbertura, horaFechamento);
        if (tipo == TipoPontoApoio.COMERCIO) {
            throw new IllegalArgumentException("Orgao publico nao pode ser do tipo COMERCIO");
        }
    }

    /**
     * Orgao publico tem a maior prioridade
     *
     * @return 1
     */
    @Override
    public int prioridade() {
        return 1;
    }

    /**
     * Orgao publico sempre pode ser sugerido
     *
     * @return true
     */
    @Override
    public boolean podeSerSugerido() {
        return true;
    }

    /**
     * Mostra o orgao publico em uma linha
     *
     * @return texto do ponto com a indicacao de orgao publico
     */
    @Override
    public String toString() {
        return super.toString() + " | orgao publico";
    }
}
