package br.com.fiap.caminhoseguro.repositorio;

import br.com.fiap.caminhoseguro.dominio.Ocorrencia;
import br.com.fiap.caminhoseguro.dominio.Trecho;

import java.util.ArrayList;

/**
 * Guarda as ocorrencias oficiais e os reports da comunidade em memoria
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class OcorrenciaRepositorio {

    private ArrayList<Ocorrencia> ocorrencias;

    /**
     * Cria o repositorio vazio
     */
    public OcorrenciaRepositorio() {
        this.ocorrencias = new ArrayList<Ocorrencia>();
    }

    /**
     * Registra uma ocorrencia e liga ela ao seu trecho
     *
     * @param ocorrencia ocorrencia a ser registrada
     *
     * @throws IllegalArgumentException se a ocorrencia for nula ou o id ja existir
     */
    public void adicionar(Ocorrencia ocorrencia) {
        if (ocorrencia == null) {
            throw new IllegalArgumentException("Ocorrencia nao pode ser nula");
        }
        if (buscarPorId(ocorrencia.getId()) != null) {
            throw new IllegalArgumentException("Ja existe uma ocorrencia com o id " + ocorrencia.getId());
        }
        ocorrencias.add(ocorrencia);
        ocorrencia.getTrecho().adicionarOcorrencia(ocorrencia);
    }

    /**
     * Lista todas as ocorrencias
     *
     * @return copia da lista de ocorrencias
     */
    public ArrayList<Ocorrencia> listarTodos() {
        return new ArrayList<Ocorrencia>(ocorrencias);
    }

    /**
     * Busca uma ocorrencia pelo id
     *
     * @param id identificador procurado
     * @return ocorrencia encontrada ou null
     */
    public Ocorrencia buscarPorId(int id) {
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getId() == id) {
                return ocorrencia;
            }
        }
        return null;
    }

    /**
     * Busca as ocorrencias de um trecho
     *
     * @param trecho trecho usado no filtro
     * @return ocorrencias do trecho informado
     */
    public ArrayList<Ocorrencia> buscarPorTrecho(Trecho trecho) {
        ArrayList<Ocorrencia> resultado = new ArrayList<Ocorrencia>();
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getTrecho() == trecho) {
                resultado.add(ocorrencia);
            }
        }
        return resultado;
    }

}
