package br.com.fiap.caminhoseguro.repositorio;

import br.com.fiap.caminhoseguro.dominio.Bairro;
import br.com.fiap.caminhoseguro.dominio.Trecho;

import java.util.ArrayList;

/**
 * Guarda os trechos de rua em memoria
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class TrechoRepositorio {

    private ArrayList<Trecho> trechos;

    /**
     * Cria o repositorio vazio
     */
    public TrechoRepositorio() {
        this.trechos = new ArrayList<Trecho>();
    }

    /**
     * Adiciona um trecho
     *
     * @param trecho trecho a ser adicionado
     *
     * @throws IllegalArgumentException se o trecho for nulo ou o id ja existir
     */
    public void adicionar(Trecho trecho) {
        if (trecho == null) {
            throw new IllegalArgumentException("Trecho nao pode ser nulo");
        }
        if (buscarPorId(trecho.getId()) != null) {
            throw new IllegalArgumentException("Ja existe um trecho com o id " + trecho.getId());
        }
        trechos.add(trecho);
    }

    /**
     * Lista todos os trechos
     *
     * @return copia da lista de trechos
     */
    public ArrayList<Trecho> listarTodos() {
        return new ArrayList<Trecho>(trechos);
    }

    /**
     * Busca um trecho pelo id
     *
     * @param id identificador procurado
     * @return trecho encontrado ou null
     */
    public Trecho buscarPorId(int id) {
        for (Trecho trecho : trechos) {
            if (trecho.getId() == id) {
                return trecho;
            }
        }
        return null;
    }

    /**
     * Busca os trechos de um bairro
     *
     * @param bairro bairro usado no filtro
     * @return trechos do bairro informado
     */
    public ArrayList<Trecho> buscarPorBairro(Bairro bairro) {
        ArrayList<Trecho> resultado = new ArrayList<Trecho>();
        for (Trecho trecho : trechos) {
            if (trecho.getBairro() == bairro) {
                resultado.add(trecho);
            }
        }
        return resultado;
    }
}
