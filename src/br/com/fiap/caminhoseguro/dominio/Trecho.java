package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.Iluminacao;
import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.util.Validacao;

import java.util.ArrayList;

/**
 * Trecho de rua usado para montar as rotas, com a sua iluminacao e as ocorrencias registradas
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Trecho {

    private final int id;
    private String nomeRua;
    private Bairro bairro;
    private int distanciaMetros;
    private Iluminacao iluminacao;
    private final double posicaoX;
    private final double posicaoY;
    private ArrayList<Ocorrencia> ocorrencias;

    /**
     * Cria um trecho de rua sem ocorrencias
     *
     * @param id identificador do trecho
     * @param nomeRua nome da rua do trecho
     * @param bairro bairro onde fica o trecho
     * @param distanciaMetros distancia do trecho em metros
     * @param iluminacao qualidade da iluminacao do trecho
     * @param posicaoX coordenada X do trecho no plano do bairro, em metros
     * @param posicaoY coordenada Y do trecho no plano do bairro, em metros
     *
     * @throws IllegalArgumentException se o nome estiver vazio, o bairro ou a iluminacao forem nulos ou a distancia for menor ou igual a 0
     */
    public Trecho(int id, String nomeRua, Bairro bairro, int distanciaMetros, Iluminacao iluminacao,
                  double posicaoX, double posicaoY) {
        this.id = id;
        setNomeRua(nomeRua);
        setBairro(bairro);
        setDistanciaMetros(distanciaMetros);
        setIluminacao(iluminacao);
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.ocorrencias = new ArrayList<Ocorrencia>();
    }

    /**
     * Liga uma ocorrencia a este trecho
     *
     * @param ocorrencia ocorrencia registrada neste trecho
     *
     * @throws IllegalArgumentException se a ocorrencia for nula ou pertencer a outro trecho
     */
    public void adicionarOcorrencia(Ocorrencia ocorrencia) {
        Validacao.obrigatorio(ocorrencia, "Ocorrencia nao pode ser nula");
        if (ocorrencia.getTrecho() != this) {
            throw new IllegalArgumentException("A ocorrencia " + ocorrencia.getId() + " pertence a outro trecho");
        }
        ocorrencias.add(ocorrencia);
    }

    /**
     * Conta quantas ocorrencias de um tipo existem neste trecho
     *
     * @param tipo tipo de ocorrencia a ser contado
     * @return quantidade de ocorrencias do tipo informado
     */
    public int contarOcorrencias(TipoOcorrencia tipo) {
        int quantidade = 0;
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getTipo() == tipo) {
                quantidade++;
            }
        }
        return quantidade;
    }

    /**
     * Devolve o identificador do trecho
     *
     * @return identificador do trecho
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o nome da rua do trecho
     *
     * @return nome da rua do trecho
     */
    public String getNomeRua() {
        return nomeRua;
    }

    /**
     * Altera o nome da rua do trecho
     *
     * @param nomeRua nome da rua do trecho
     *
     * @throws IllegalArgumentException se o nome estiver vazio
     */
    public void setNomeRua(String nomeRua) {
        Validacao.textoObrigatorio(nomeRua, "Nome da rua nao pode ser vazio");
        this.nomeRua = nomeRua;
    }

    /**
     * Devolve o bairro onde fica o trecho
     *
     * @return bairro onde fica o trecho
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * Altera o bairro do trecho
     *
     * @param bairro bairro onde fica o trecho
     *
     * @throws IllegalArgumentException se o bairro for nulo
     */
    public void setBairro(Bairro bairro) {
        Validacao.obrigatorio(bairro, "Bairro do trecho nao pode ser nulo");
        this.bairro = bairro;
    }

    /**
     * Devolve a distancia do trecho em metros
     *
     * @return distancia do trecho em metros
     */
    public int getDistanciaMetros() {
        return distanciaMetros;
    }

    /**
     * Altera a distancia do trecho em metros
     *
     * @param distanciaMetros distancia do trecho em metros
     *
     * @throws IllegalArgumentException se a distancia for menor ou igual a 0
     */
    public void setDistanciaMetros(int distanciaMetros) {
        if (distanciaMetros <= 0) {
            throw new IllegalArgumentException("Distancia do trecho deve ser maior que 0, recebido: " + distanciaMetros);
        }
        this.distanciaMetros = distanciaMetros;
    }

    /**
     * Devolve a qualidade da iluminacao do trecho
     *
     * @return qualidade da iluminacao do trecho
     */
    public Iluminacao getIluminacao() {
        return iluminacao;
    }

    /**
     * Altera a iluminacao do trecho
     *
     * @param iluminacao qualidade da iluminacao do trecho
     *
     * @throws IllegalArgumentException se a iluminacao for nula
     */
    public void setIluminacao(Iluminacao iluminacao) {
        Validacao.obrigatorio(iluminacao, "Iluminacao do trecho nao pode ser nula");
        this.iluminacao = iluminacao;
    }

    /**
     * Devolve a coordenada X do trecho no plano do bairro, em metros
     *
     * @return coordenada X do trecho no plano do bairro, em metros
     */
    public double getPosicaoX() {
        return posicaoX;
    }

    /**
     * Devolve a coordenada Y do trecho no plano do bairro, em metros
     *
     * @return coordenada Y do trecho no plano do bairro, em metros
     */
    public double getPosicaoY() {
        return posicaoY;
    }

    /**
     * Devolve uma copia da lista de ocorrencias, para que ninguem altere a lista por fora
     *
     * @return copia das ocorrencias do trecho
     */
    public ArrayList<Ocorrencia> getOcorrencias() {
        return new ArrayList<Ocorrencia>(ocorrencias);
    }

    /**
     * Mostra o trecho em uma linha
     *
     * @return texto com rua, distancia e iluminacao
     */
    @Override
    public String toString() {
        return "Trecho " + id + " - " + nomeRua + " (" + distanciaMetros + " m, iluminacao "
                + iluminacao.name().toLowerCase() + ")";
    }
}
