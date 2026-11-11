package br.com.fiap.caminhoseguro.servico;

import br.com.fiap.caminhoseguro.dominio.Horario;
import br.com.fiap.caminhoseguro.dominio.Ocorrencia;
import br.com.fiap.caminhoseguro.dominio.OcorrenciaOficial;
import br.com.fiap.caminhoseguro.dominio.PerfilDeslocamento;
import br.com.fiap.caminhoseguro.dominio.ReportComunitario;
import br.com.fiap.caminhoseguro.dominio.Trecho;
import br.com.fiap.caminhoseguro.enums.Iluminacao;
import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;

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
     * Calcula a penalidade da iluminacao: BOA 0, REGULAR 1 e RUIM 3, multiplicada por 1.5 a noite
     *
     * @param iluminacao iluminacao do trecho
     * @param hora hora do deslocamento, de 0 a 23
     * @return penalidade da iluminacao
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public double calcularPenalidadeIluminacao(Iluminacao iluminacao, int hora) {
        double penalidade = 0;
        if (iluminacao == Iluminacao.REGULAR) {
            penalidade = 1;
        } else if (iluminacao == Iluminacao.RUIM) {
            penalidade = 3;
        }
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
            // polimorfismo: report e ocorrencia oficial calculam o peso de formas diferentes
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

        // agrupa as ocorrencias por tipo para gerar frases como "2 reports recentes de assedio confirmados"
        for (TipoOcorrencia tipo : TipoOcorrencia.values()) {
            int recentesConfirmados = 0;
            int recentesSemConfirmacao = 0;
            int antigos = 0;
            int oficiais = 0;
            String fonte = "";

            for (Ocorrencia ocorrencia : trecho.getOcorrencias()) {
                if (ocorrencia.getTipo() == tipo) {
                    if (ocorrencia instanceof ReportComunitario) {
                        ReportComunitario report = (ReportComunitario) ocorrencia;
                        if (report.calcularFatorTempo(agora) < 1.0) {
                            antigos++;
                        } else if (report.estaConfirmado()) {
                            recentesConfirmados++;
                        } else {
                            recentesSemConfirmacao++;
                        }
                    } else if (ocorrencia instanceof OcorrenciaOficial) {
                        OcorrenciaOficial oficial = (OcorrenciaOficial) ocorrencia;
                        oficiais++;
                        fonte = oficial.getFonte();
                    }
                }
            }

            String nomeTipo = tipo.getDescricao();
            if (recentesConfirmados > 0) {
                motivos.add(rua + ": " + contar(recentesConfirmados, "report recente", "reports recentes")
                        + " de " + nomeTipo + " " + escolher(recentesConfirmados, "confirmado", "confirmados"));
            }
            if (recentesSemConfirmacao > 0) {
                motivos.add(rua + ": " + contar(recentesSemConfirmacao, "report recente", "reports recentes")
                        + " de " + nomeTipo + " ainda sem confirmacao (peso reduzido)");
            }
            if (antigos > 0) {
                motivos.add(rua + ": " + contar(antigos, "report antigo", "reports antigos")
                        + " de " + nomeTipo + " (mais de 24 h, peso reduzido)");
            }
            if (oficiais > 0) {
                motivos.add(rua + ": " + contar(oficiais, "ocorrencia oficial", "ocorrencias oficiais")
                        + " de " + nomeTipo + " (" + fonte + ")");
            }
        }
        return motivos;
    }

    private String contar(int quantidade, String singular, String plural) {
        return quantidade + " " + escolher(quantidade, singular, plural);
    }

    private String escolher(int quantidade, String singular, String plural) {
        if (quantidade == 1) {
            return singular;
        }
        return plural;
    }
}
