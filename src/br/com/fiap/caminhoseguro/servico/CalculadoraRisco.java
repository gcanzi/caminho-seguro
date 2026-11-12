package br.com.fiap.caminhoseguro.servico;

import br.com.fiap.caminhoseguro.dominio.Ocorrencia;
import br.com.fiap.caminhoseguro.dominio.PerfilDeslocamento;
import br.com.fiap.caminhoseguro.dominio.Trecho;
import br.com.fiap.caminhoseguro.enums.Iluminacao;
import br.com.fiap.caminhoseguro.util.Horario;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Calcula o risco de 0 a 10 de um trecho e explica os motivos em linguagem simples
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class CalculadoraRisco {

    /** Menor risco possivel de um trecho */
    public static final double RISCO_MINIMO = 0.0;
    /** Maior risco possivel de um trecho */
    public static final double RISCO_MAXIMO = 10.0;
    /** Multiplicador da penalidade de iluminacao a noite */
    public static final double MULTIPLICADOR_NOITE = 1.5;

    /**
     * Cria a calculadora de risco
     */
    public CalculadoraRisco() {
    }

    /**
     * Calcula a penalidade da iluminacao (definida no enum Iluminacao), multiplicada por 1.5 a noite
     *
     * @param iluminacao iluminacao do trecho
     * @param hora hora do deslocamento, de 0 a 23
     * @return penalidade da iluminacao
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public double calcularPenalidadeIluminacao(Iluminacao iluminacao, int hora) {
        double penalidade = iluminacao.getPenalidade();
        if (Horario.ehNoite(hora)) {
            penalidade = penalidade * MULTIPLICADOR_NOITE;
        }
        return penalidade;
    }

    /**
     * Calcula o risco do trecho: soma dos pesos das ocorrencias + iluminacao, ajustado pelo perfil e limitado de 0 a 10
     *
     * @param trecho trecho avaliado
     * @param hora hora do deslocamento, de 0 a 23
     * @param agora data e hora de referencia, usada para saber se os reports sao recentes
     * @param perfil perfil de deslocamento da pessoa
     * @return risco do trecho de 0 a 10
     *
     * @throws IllegalArgumentException se algum dado for nulo ou a hora estiver fora de 0 a 23
     */
    public double calcularRisco(Trecho trecho, int hora, LocalDateTime agora, PerfilDeslocamento perfil) {
        if (trecho == null || agora == null || perfil == null) {
            throw new IllegalArgumentException("Trecho, data de referencia e perfil sao obrigatorios");
        }
        double risco = 0;
        for (Ocorrencia ocorrencia : trecho.getOcorrencias()) {
            risco = risco + ocorrencia.calcularPeso(agora);
        }
        risco = risco + calcularPenalidadeIluminacao(trecho.getIluminacao(), hora);
        risco = perfil.ajustarRisco(trecho, risco, hora);

        if (risco < RISCO_MINIMO) {
            risco = RISCO_MINIMO;
        }
        if (risco > RISCO_MAXIMO) {
            risco = RISCO_MAXIMO;
        }
        return risco;
    }

    /**
     * Explica em linguagem simples o que aumenta o risco do trecho
     *
     * @param trecho trecho avaliado
     * @param hora hora do deslocamento, de 0 a 23
     * @param agora data e hora de referencia, usada para saber se os reports sao recentes
     * @return lista de motivos; vazia se o trecho nao tiver nenhum alerta
     *
     * @throws IllegalArgumentException se o trecho ou a data forem nulos ou a hora estiver fora de 0 a 23
     */
    public ArrayList<String> explicarRisco(Trecho trecho, int hora, LocalDateTime agora) {
        if (trecho == null || agora == null) {
            throw new IllegalArgumentException("Trecho e data de referencia sao obrigatorios");
        }
        ArrayList<String> motivos = new ArrayList<String>();
        String rua = trecho.getNomeRua();

        if (trecho.getIluminacao() != Iluminacao.BOA) {
            String texto = rua + ": trecho de " + trecho.getDistanciaMetros() + " m com iluminacao "
                    + trecho.getIluminacao().name().toLowerCase();
            if (Horario.ehNoite(hora)) {
                texto = texto + " a noite";
            }
            motivos.add(texto);
        }

        ArrayList<String> textos = new ArrayList<String>();
        ArrayList<Integer> quantidades = new ArrayList<Integer>();
        for (Ocorrencia ocorrencia : trecho.getOcorrencias()) {
            String texto = ocorrencia.explicarMotivo(agora);
            int posicao = textos.indexOf(texto);
            if (posicao == -1) {
                textos.add(texto);
                quantidades.add(1);
            } else {
                quantidades.set(posicao, quantidades.get(posicao) + 1);
            }
        }
        for (int i = 0; i < textos.size(); i++) {
            String quantidade = "";
            if (quantidades.get(i) > 1) {
                quantidade = quantidades.get(i) + "x ";
            }
            motivos.add(rua + ": " + quantidade + textos.get(i));
        }
        return motivos;
    }
}
