package br.com.fiap.caminhoseguro.repositorio;

import br.com.fiap.caminhoseguro.dominio.PontoApoio;
import br.com.fiap.caminhoseguro.util.Validacao;

import java.util.ArrayList;

/**
 * Guarda os pontos de apoio (orgaos publicos e comercios parceiros) em memoria
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class PontoApoioRepositorio {

    private ArrayList<PontoApoio> pontos;

    /**
     * Cria o repositorio vazio
     */
    public PontoApoioRepositorio() {
        this.pontos = new ArrayList<PontoApoio>();
    }

    /**
     * Adiciona um ponto de apoio
     *
     * @param ponto ponto de apoio a ser adicionado
     *
     * @throws IllegalArgumentException se o ponto for nulo ou o id ja existir
     */
    public void adicionar(PontoApoio ponto) {
        Validacao.obrigatorio(ponto, "Ponto de apoio nao pode ser nulo");
        if (buscarPorId(ponto.getId()) != null) {
            throw new IllegalArgumentException("Ja existe um ponto de apoio com o id " + ponto.getId());
        }
        pontos.add(ponto);
    }

    /**
     * Lista todos os pontos de apoio
     *
     * @return copia da lista de pontos
     */
    public ArrayList<PontoApoio> listarTodos() {
        return new ArrayList<PontoApoio>(pontos);
    }

    /**
     * Busca um ponto de apoio pelo id
     *
     * @param id identificador procurado
     * @return ponto encontrado ou null
     */
    public PontoApoio buscarPorId(int id) {
        for (PontoApoio ponto : pontos) {
            if (ponto.getId() == id) {
                return ponto;
            }
        }
        return null;
    }

}
