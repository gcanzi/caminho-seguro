package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.util.Validacao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ocorrencia em um trecho de rua; cada tipo de ocorrencia calcula o seu proprio peso no risco
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public abstract class Ocorrencia {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private Trecho trecho;
    private TipoOcorrencia tipo;
    private LocalDateTime dataHora;

    /**
     * Cria uma ocorrencia em um trecho
     *
     * @param id identificador da ocorrencia
     * @param trecho trecho onde a ocorrencia aconteceu
     * @param tipo tipo da ocorrencia
     * @param dataHora data e hora da ocorrencia
     *
     * @throws IllegalArgumentException se o trecho, o tipo ou a data e hora forem nulos
     */
    public Ocorrencia(int id, Trecho trecho, TipoOcorrencia tipo, LocalDateTime dataHora) {
        this.id = id;
        setTrecho(trecho);
        setTipo(tipo);
        setDataHora(dataHora);
    }

    /**
     * Calcula o peso da ocorrencia no risco do trecho
     *
     * @param agora data e hora de referencia da consulta
     * @return peso da ocorrencia no momento informado
     */
    public abstract double calcularPeso(LocalDateTime agora);

    /**
     * Explica em linguagem simples por que a ocorrencia aumenta o risco do trecho
     *
     * @param agora data e hora de referencia da consulta
     * @return frase curta com o motivo, sem o nome da rua
     */
    public abstract String explicarMotivo(LocalDateTime agora);

    /**
     * Calcula quantas horas se passaram desde a ocorrencia
     *
     * @param agora data e hora de referencia da consulta
     * @return horas desde a ocorrencia, nunca negativo
     */
    public long calcularHorasDesde(LocalDateTime agora) {
        long horas = Duration.between(dataHora, agora).toHours();
        if (horas < 0) {
            return 0;
        }
        return horas;
    }

    /**
     * Descreve a ocorrencia em linguagem simples
     *
     * @return texto com tipo, rua e data
     */
    public String descrever() {
        return tipo.getDescricao() + " em " + trecho.getNomeRua() + " - " + dataHora.format(FORMATO);
    }

    /**
     * Devolve o identificador da ocorrencia
     *
     * @return identificador da ocorrencia
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o trecho onde a ocorrencia aconteceu
     *
     * @return trecho onde a ocorrencia aconteceu
     */
    public Trecho getTrecho() {
        return trecho;
    }

    /**
     * Altera o trecho da ocorrencia
     *
     * @param trecho trecho onde a ocorrencia aconteceu
     *
     * @throws IllegalArgumentException se o trecho for nulo
     */
    private void setTrecho(Trecho trecho) {
        Validacao.obrigatorio(trecho, "Trecho da ocorrencia nao pode ser nulo");
        this.trecho = trecho;
    }

    /**
     * Devolve o tipo da ocorrencia
     *
     * @return tipo da ocorrencia
     */
    public TipoOcorrencia getTipo() {
        return tipo;
    }

    /**
     * Altera o tipo da ocorrencia
     *
     * @param tipo tipo da ocorrencia
     *
     * @throws IllegalArgumentException se o tipo for nulo
     */
    public void setTipo(TipoOcorrencia tipo) {
        Validacao.obrigatorio(tipo, "Tipo da ocorrencia nao pode ser nulo");
        this.tipo = tipo;
    }

    /**
     * Devolve a data e hora da ocorrencia
     *
     * @return data e hora da ocorrencia
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Altera a data e hora da ocorrencia
     *
     * @param dataHora data e hora da ocorrencia
     *
     * @throws IllegalArgumentException se a data e hora forem nulas
     */
    public void setDataHora(LocalDateTime dataHora) {
        Validacao.obrigatorio(dataHora, "Data e hora da ocorrencia nao podem ser nulas");
        this.dataHora = dataHora;
    }

    /**
     * Mostra a ocorrencia em uma linha
     *
     * @return texto com id e descricao
     */
    @Override
    public String toString() {
        return "Ocorrencia " + id + ": " + descrever();
    }
}
