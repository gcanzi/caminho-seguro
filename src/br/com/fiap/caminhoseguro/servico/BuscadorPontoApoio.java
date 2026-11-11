package br.com.fiap.caminhoseguro.servico;

import br.com.fiap.caminhoseguro.dominio.EstabelecimentoParceiro;
import br.com.fiap.caminhoseguro.dominio.PontoApoio;
import br.com.fiap.caminhoseguro.repositorio.PontoApoioRepositorio;

import java.util.ArrayList;

/**
 * Busca o Ponto Seguro aberto mais proximo para quem apertou o botao "Me sinto insegura"
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class BuscadorPontoApoio {

    /** Diferenca de distancia, em metros, considerada parecida */
    public static final double DIFERENCA_PARECIDA_METROS = 100.0;

    private PontoApoioRepositorio repositorio;

    /**
     * Cria o buscador usando o repositorio de pontos de apoio
     *
     * @param repositorio repositorio com os pontos de apoio cadastrados
     *
     * @throws IllegalArgumentException se o repositorio for nulo
     */
    public BuscadorPontoApoio(PontoApoioRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("Repositorio de pontos de apoio nao pode ser nulo");
        }
        this.repositorio = repositorio;
    }

    /**
     * Busca o ponto aberto e que pode ser sugerido mais proximo; se a distancia for parecida
     * (ate 100 m de diferenca), prefere o de menor prioridade, ou seja, o orgao publico
     *
     * @param x coordenada X da pessoa, em metros
     * @param y coordenada Y da pessoa, em metros
     * @param hora hora do pedido de ajuda, de 0 a 23
     * @return ponto de apoio sugerido, ou null se nenhum estiver disponivel
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public PontoApoio buscarMaisProximo(double x, double y, int hora) {
        PontoApoio melhor = null;
        double distanciaMelhor = 0;

        for (PontoApoio ponto : repositorio.listarTodos()) {
            if (ponto.estaAberto(hora) && ponto.podeSerSugerido()) {
                double distancia = ponto.calcularDistancia(x, y);
                if (melhor == null) {
                    melhor = ponto;
                    distanciaMelhor = distancia;
                } else if (Math.abs(distancia - distanciaMelhor) <= DIFERENCA_PARECIDA_METROS) {
                    // distancia parecida: desempata pela prioridade e depois pela distancia
                    if (ponto.prioridade() < melhor.prioridade()
                            || (ponto.prioridade() == melhor.prioridade() && distancia < distanciaMelhor)) {
                        melhor = ponto;
                        distanciaMelhor = distancia;
                    }
                } else if (distancia < distanciaMelhor) {
                    melhor = ponto;
                    distanciaMelhor = distancia;
                }
            }
        }
        return melhor;
    }

    /**
     * Explica por que cada ponto fechado ou bloqueado nao entrou na busca
     *
     * @param hora hora do pedido de ajuda, de 0 a 23
     * @return lista com o nome de cada ponto descartado e o motivo
     */
    public ArrayList<String> explicarDescartes(int hora) {
        ArrayList<String> descartes = new ArrayList<String>();
        for (PontoApoio ponto : repositorio.listarTodos()) {
            if (!ponto.estaAberto(hora)) {
                descartes.add(ponto.getNome() + ": fechado as " + hora + "h (" + ponto.descreverHorario() + ")");
            } else if (!ponto.podeSerSugerido()) {
                String motivo = "nao pode ser sugerido";
                if (ponto instanceof EstabelecimentoParceiro) {
                    EstabelecimentoParceiro parceiro = (EstabelecimentoParceiro) ponto;
                    motivo = parceiro.descreverSituacao();
                }
                descartes.add(ponto.getNome() + ": " + motivo);
            }
        }
        return descartes;
    }
}
