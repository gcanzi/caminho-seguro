package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.util.Validacao;

import java.time.LocalDateTime;

/**
 * Ocorrencia vinda de uma fonte oficial, como a SSP-SP, com gravidade de 1 a 5
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class OcorrenciaOficial extends Ocorrencia {

    private String fonte;
    /** Gravidade media: com ela o peso da ocorrencia fica igual ao peso do tipo */
    public static final double GRAVIDADE_REFERENCIA = 3.0;

    private int gravidade;

    /**
     * Cria uma ocorrencia oficial
     *
     * @param id identificador da ocorrencia
     * @param trecho trecho onde a ocorrencia aconteceu
     * @param tipo tipo da ocorrencia
     * @param dataHora data e hora da ocorrencia
     * @param fonte fonte oficial da ocorrencia, por exemplo SSP-SP
     * @param gravidade gravidade da ocorrencia, de 1 a 5
     *
     * @throws IllegalArgumentException se a fonte estiver vazia ou a gravidade estiver fora de 1 a 5
     */
    public OcorrenciaOficial(int id, Trecho trecho, TipoOcorrencia tipo, LocalDateTime dataHora,
                             String fonte, int gravidade) {
        super(id, trecho, tipo, dataHora);
        setFonte(fonte);
        setGravidade(gravidade);
    }

    /**
     * Calcula o peso da ocorrencia oficial: peso do tipo x gravidade / 3
     *
     * @param agora data e hora de referencia da consulta
     * @return peso da ocorrencia no risco do trecho
     */
    @Override
    public double calcularPeso(LocalDateTime agora) {
        return getTipo().getPeso() * gravidade / GRAVIDADE_REFERENCIA;
    }

    /**
     * Explica o motivo citando a fonte oficial
     *
     * @param agora data e hora de referencia da consulta
     * @return frase com o tipo e a fonte da ocorrencia
     */
    @Override
    public String explicarMotivo(LocalDateTime agora) {
        return "ocorrencia oficial de " + getTipo().getDescricao() + " (" + fonte + ")";
    }

    /**
     * Descreve a ocorrencia com a fonte e a gravidade
     *
     * @return texto da ocorrencia oficial
     */
    @Override
    public String descrever() {
        return "oficial de " + super.descrever() + " (fonte " + fonte + ", gravidade " + gravidade + ")";
    }

    /**
     * Devolve a fonte oficial da ocorrencia, por exemplo SSP-SP
     *
     * @return fonte oficial da ocorrencia, por exemplo SSP-SP
     */
    public String getFonte() {
        return fonte;
    }

    /**
     * Altera a fonte da ocorrencia
     *
     * @param fonte fonte oficial da ocorrencia, por exemplo SSP-SP
     *
     * @throws IllegalArgumentException se a fonte estiver vazia
     */
    public void setFonte(String fonte) {
        Validacao.textoObrigatorio(fonte, "Fonte da ocorrencia oficial nao pode ser vazia");
        this.fonte = fonte;
    }

    /**
     * Devolve a gravidade da ocorrencia, de 1 a 5
     *
     * @return gravidade da ocorrencia, de 1 a 5
     */
    public int getGravidade() {
        return gravidade;
    }

    /**
     * Altera a gravidade da ocorrencia
     *
     * @param gravidade gravidade da ocorrencia, de 1 a 5
     *
     * @throws IllegalArgumentException se a gravidade estiver fora de 1 a 5
     */
    public void setGravidade(int gravidade) {
        Validacao.faixa(gravidade, 1, 5, "Gravidade");
        this.gravidade = gravidade;
    }
}
