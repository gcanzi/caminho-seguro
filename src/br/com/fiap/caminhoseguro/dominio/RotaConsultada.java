package br.com.fiap.caminhoseguro.dominio;

import java.util.ArrayList;

/**
 * Rota candidata de A a B, formada por trechos, com nota de seguranca e os motivos da nota
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class RotaConsultada {

    private final int id;
    private String descricao;
    private ArrayList<Trecho> trechos;
    private double notaSeguranca;
    private ArrayList<String> motivos;

    /**
     * Cria uma rota sem trechos e ainda sem nota
     *
     * @param id identificador da rota
     * @param descricao descricao da rota
     *
     * @throws IllegalArgumentException se a descricao estiver vazia
     */
    public RotaConsultada(int id, String descricao) {
        this.id = id;
        setDescricao(descricao);
        this.trechos = new ArrayList<Trecho>();
        this.motivos = new ArrayList<String>();
    }

    /**
     * Adiciona o proximo trecho da rota
     *
     * @param trecho proximo trecho da rota
     *
     * @throws IllegalArgumentException se o trecho for nulo
     */
    public void adicionarTrecho(Trecho trecho) {
        Validacao.obrigatorio(trecho, "Trecho da rota nao pode ser nulo");
        trechos.add(trecho);
    }

    /**
     * Soma a distancia de todos os trechos
     *
     * @return distancia total da rota em metros
     */
    public int calcularDistanciaTotal() {
        int total = 0;
        for (Trecho trecho : trechos) {
            total = total + trecho.getDistanciaMetros();
        }
        return total;
    }

    /**
     * Adiciona um motivo que explica a nota da rota
     *
     * @param motivo motivo que explica a nota
     *
     * @throws IllegalArgumentException se o motivo estiver vazio
     */
    public void adicionarMotivo(String motivo) {
        Validacao.textoObrigatorio(motivo, "Motivo nao pode ser vazio");
        motivos.add(motivo);
    }

    /**
     * Apaga os motivos, para avaliar a rota de novo com outro perfil ou horario
     */
    public void limparMotivos() {
        motivos.clear();
    }

    /**
     * Devolve o identificador da rota
     *
     * @return identificador da rota
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve a descricao da rota
     *
     * @return descricao da rota
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Altera a descricao da rota
     *
     * @param descricao descricao da rota
     *
     * @throws IllegalArgumentException se a descricao estiver vazia
     */
    public void setDescricao(String descricao) {
        Validacao.textoObrigatorio(descricao, "Descricao da rota nao pode ser vazia");
        this.descricao = descricao;
    }

    /**
     * Devolve a nota de seguranca da rota, de 0 a 100
     *
     * @return nota de seguranca da rota, de 0 a 100
     */
    public double getNotaSeguranca() {
        return notaSeguranca;
    }

    /**
     * Altera a nota de seguranca da rota
     *
     * @param notaSeguranca nota de seguranca da rota, de 0 a 100
     *
     * @throws IllegalArgumentException se a nota estiver fora de 0 a 100
     */
    public void setNotaSeguranca(double notaSeguranca) {
        Validacao.faixa(notaSeguranca, 0, 100, "Nota de seguranca");
        this.notaSeguranca = notaSeguranca;
    }

    /**
     * Devolve uma copia dos trechos da rota
     *
     * @return copia dos trechos
     */
    public ArrayList<Trecho> getTrechos() {
        return new ArrayList<Trecho>(trechos);
    }

    /**
     * Devolve uma copia dos motivos da nota
     *
     * @return copia dos motivos
     */
    public ArrayList<String> getMotivos() {
        return new ArrayList<String>(motivos);
    }

    /**
     * Mostra a rota em uma linha
     *
     * @return texto com descricao, distancia e nota
     */
    @Override
    public String toString() {
        return descricao + " - " + calcularDistanciaTotal() + " m, nota "
                + Formatacao.decimal(notaSeguranca) + "/100";
    }
}
