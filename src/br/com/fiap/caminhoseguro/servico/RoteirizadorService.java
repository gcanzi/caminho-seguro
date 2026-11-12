package br.com.fiap.caminhoseguro.servico;

import br.com.fiap.caminhoseguro.dominio.PerfilDeslocamento;
import br.com.fiap.caminhoseguro.dominio.RotaConsultada;
import br.com.fiap.caminhoseguro.dominio.Trecho;
import br.com.fiap.caminhoseguro.dominio.Validacao;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Compara as rotas candidatas de A a B e escolhe a mais rapida, a mais segura e a equilibrada.
 * Recebe as rotas candidatas ja montadas e so calcula a nota de seguranca
 * de cada uma e compara as rotas.
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class RoteirizadorService {

    /** Maior nota de seguranca de uma rota, quando o risco e 0 */
    public static final double NOTA_MAXIMA = 100.0;
    /** Pontos perdidos na nota para cada ponto de risco */
    public static final double PONTOS_POR_RISCO = 10.0;

    private CalculadoraRisco calculadora;

    /**
     * Cria o roteirizador usando a calculadora de risco informada
     *
     * @param calculadora calculadora usada para o risco de cada trecho
     *
     * @throws IllegalArgumentException se a calculadora for nula
     */
    public RoteirizadorService(CalculadoraRisco calculadora) {
        Validacao.obrigatorio(calculadora, "Calculadora de risco nao pode ser nula");
        this.calculadora = calculadora;
    }

    /**
     * Calcula o risco medio da rota, ponderado pela distancia de cada trecho
     *
     * @param rota rota candidata
     * @param hora hora do deslocamento, de 0 a 23
     * @param agora data e hora de referencia da consulta
     * @param perfil perfil de deslocamento da pessoa
     * @return risco medio de 0 a 10
     *
     * @throws IllegalArgumentException se a rota for nula ou nao tiver trechos
     */
    public double calcularRiscoMedio(RotaConsultada rota, int hora, LocalDateTime agora, PerfilDeslocamento perfil) {
        Validacao.obrigatorio(rota, "Rota nao pode ser nula");
        int distanciaTotal = rota.calcularDistanciaTotal();
        if (distanciaTotal == 0) {
            throw new IllegalArgumentException("A rota " + rota.getDescricao() + " nao tem trechos");
        }
        double soma = 0;
        for (Trecho trecho : rota.getTrechos()) {
            soma = soma + calculadora.calcularRisco(trecho, hora, agora, perfil) * trecho.getDistanciaMetros();
        }
        return soma / distanciaTotal;
    }

    /**
     * Calcula a nota de seguranca (100 - risco medio x 10) e preenche os motivos da rota
     *
     * @param rota rota candidata
     * @param hora hora do deslocamento, de 0 a 23
     * @param agora data e hora de referencia da consulta
     * @param perfil perfil de deslocamento da pessoa
     */
    public void avaliarRota(RotaConsultada rota, int hora, LocalDateTime agora, PerfilDeslocamento perfil) {
        double riscoMedio = calcularRiscoMedio(rota, hora, agora, perfil);
        rota.setNotaSeguranca(NOTA_MAXIMA - riscoMedio * PONTOS_POR_RISCO);

        rota.limparMotivos();
        for (Trecho trecho : rota.getTrechos()) {
            for (String motivo : calculadora.explicarRisco(trecho, hora, agora)) {
                rota.adicionarMotivo(motivo);
            }
        }
        if (rota.getMotivos().isEmpty()) {
            rota.adicionarMotivo("Nenhum alerta registrado nos trechos desta rota");
        }
    }

    /**
     * Avalia todas as rotas candidatas
     *
     * @param rotas rotas candidatas de A a B
     * @param hora hora do deslocamento, de 0 a 23
     * @param agora data e hora de referencia da consulta
     * @param perfil perfil de deslocamento da pessoa
     */
    public void avaliarRotas(ArrayList<RotaConsultada> rotas, int hora, LocalDateTime agora, PerfilDeslocamento perfil) {
        validarRotas(rotas);
        for (RotaConsultada rota : rotas) {
            avaliarRota(rota, hora, agora, perfil);
        }
    }

    /**
     * Calcula o custo da rota: distancia x (1 + alfa x riscoMedio / 10)
     *
     * @param rota rota candidata
     * @param alfa quanto a pessoa aceita andar a mais em troca de seguranca (0 considera so a distancia)
     * @return custo da rota; quanto menor, melhor
     *
     * @throws IllegalArgumentException se o alfa for negativo
     */
    public double calcularCusto(RotaConsultada rota, double alfa) {
        if (alfa < 0) {
            throw new IllegalArgumentException("Alfa nao pode ser negativo, recebido: " + alfa);
        }
        double riscoMedio = (NOTA_MAXIMA - rota.getNotaSeguranca()) / PONTOS_POR_RISCO;
        return rota.calcularDistanciaTotal() * (1 + alfa * riscoMedio / CalculadoraRisco.RISCO_MAXIMO);
    }

    /**
     * Escolhe a rota com menor distancia
     *
     * @param rotas rotas candidatas de A a B
     * @return rota mais rapida
     */
    public RotaConsultada escolherMaisRapida(ArrayList<RotaConsultada> rotas) {
        validarRotas(rotas);
        RotaConsultada melhor = rotas.get(0);
        for (RotaConsultada rota : rotas) {
            if (rota.calcularDistanciaTotal() < melhor.calcularDistanciaTotal()) {
                melhor = rota;
            }
        }
        return melhor;
    }

    /**
     * Escolhe a rota com maior nota de seguranca; as rotas precisam ter sido avaliadas antes
     *
     * @param rotas rotas candidatas de A a B
     * @return rota mais segura
     */
    public RotaConsultada escolherMaisSegura(ArrayList<RotaConsultada> rotas) {
        validarRotas(rotas);
        RotaConsultada melhor = rotas.get(0);
        for (RotaConsultada rota : rotas) {
            if (rota.getNotaSeguranca() > melhor.getNotaSeguranca()) {
                melhor = rota;
            }
        }
        return melhor;
    }

    /**
     * Escolhe a rota de menor custo, equilibrando distancia e seguranca
     *
     * @param rotas rotas candidatas de A a B
     * @param alfa quanto a pessoa aceita andar a mais em troca de seguranca (0 considera so a distancia)
     * @return rota equilibrada
     */
    public RotaConsultada escolherEquilibrada(ArrayList<RotaConsultada> rotas, double alfa) {
        validarRotas(rotas);
        RotaConsultada melhor = rotas.get(0);
        for (RotaConsultada rota : rotas) {
            if (calcularCusto(rota, alfa) < calcularCusto(melhor, alfa)) {
                melhor = rota;
            }
        }
        return melhor;
    }

    private void validarRotas(ArrayList<RotaConsultada> rotas) {
        if (rotas == null || rotas.isEmpty()) {
            throw new IllegalArgumentException("E preciso informar pelo menos uma rota candidata");
        }
    }
}
