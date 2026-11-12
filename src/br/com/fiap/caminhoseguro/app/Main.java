package br.com.fiap.caminhoseguro.app;

import br.com.fiap.caminhoseguro.dominio.AvaliacaoAcolhimento;
import br.com.fiap.caminhoseguro.dominio.Formatacao;
import br.com.fiap.caminhoseguro.dominio.Ocorrencia;
import br.com.fiap.caminhoseguro.dominio.OcorrenciaOficial;
import br.com.fiap.caminhoseguro.dominio.PontoApoio;
import br.com.fiap.caminhoseguro.dominio.ReportComunitario;
import br.com.fiap.caminhoseguro.dominio.RotaConsultada;
import br.com.fiap.caminhoseguro.dominio.Trecho;
import br.com.fiap.caminhoseguro.dominio.Usuario;
import br.com.fiap.caminhoseguro.enums.NivelRisco;
import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.servico.BuscadorPontoApoio;
import br.com.fiap.caminhoseguro.servico.CalculadoraRisco;
import br.com.fiap.caminhoseguro.servico.Roteirizador;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Executa o cenario do Caminho Seguro no piloto da Bela Vista, com dados simulados
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Main {

    // quanto a pessoa aceita andar a mais em troca de seguranca
    private static final double ALFA = 1.0;

    private Main() {
    }

    /**
     * Monta o cenario e imprime cada etapa do uso do app
     *
     * @param args argumentos da linha de comando (nao usados)
     */
    public static void main(String[] args) {
        CenarioBelaVista cenario = new CenarioBelaVista();
        CalculadoraRisco calculadora = new CalculadoraRisco();
        Roteirizador roteirizador = new Roteirizador(calculadora);

        imprimirBanner(cenario);
        imprimirBairro(cenario);
        imprimirOcorrencias(cenario);
        imprimirRiscos(cenario, calculadora);
        imprimirRotas(cenario, roteirizador);
        imprimirRecomendacao(cenario, calculadora, roteirizador);
        imprimirPedidoAjuda(cenario);
        imprimirPontosApoio(cenario);
        testarValidacoes(cenario);
    }

    /**
     * Imprime o cabecalho do app com o objetivo e o bairro do piloto
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void imprimirBanner(CenarioBelaVista cenario) {
        System.out.println("============================================================");
        System.out.println("  CAMINHO SEGURO");
        System.out.println("  Rota mais segura de A ate B e Ponto Seguro mais proximo");
        System.out.println("  Piloto: " + cenario.getBairro().getNome() + ", " + cenario.getBairro().getCidade()
                + " | ODS 3, 10 e 11 | dados simulados");
        System.out.println("============================================================");
        System.out.println();
    }

    /**
     * Imprime o bairro, os trechos e os usuarios do cenario
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void imprimirBairro(CenarioBelaVista cenario) {
        System.out.println("===== BAIRRO, TRECHOS E USUARIOS =====");
        System.out.println(cenario.getBairro());
        for (Trecho trecho : cenario.getTrechoRepositorio().buscarPorBairro(cenario.getBairro())) {
            System.out.println(trecho);
        }
        for (Usuario usuario : cenario.getUsuarios()) {
            System.out.println(usuario);
        }
        System.out.println();
    }

    /**
     * Imprime as ocorrencias com o peso de cada uma a noite e quantas ha em cada trecho
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void imprimirOcorrencias(CenarioBelaVista cenario) {
        System.out.println("===== OCORRENCIAS REGISTRADAS (PESO AS 23H) =====");
        for (Ocorrencia ocorrencia : cenario.getOcorrenciaRepositorio().listarTodos()) {
            System.out.println(ocorrencia + " -> peso "
                    + Formatacao.decimal(ocorrencia.calcularPeso(CenarioBelaVista.NOITE)));
        }
        System.out.println("Ocorrencias por trecho:");
        for (Trecho trecho : cenario.getTrechoRepositorio().listarTodos()) {
            System.out.println("   " + trecho.getNomeRua() + ": "
                    + cenario.getOcorrenciaRepositorio().buscarPorTrecho(trecho).size());
        }
        System.out.println();
    }

    /**
     * Imprime o risco de cada trecho a tarde e a noite, com os motivos da noite
     *
     * @param cenario cenario simulado da Bela Vista
     * @param calculadora calculadora de risco dos trechos
     */
    private static void imprimirRiscos(CenarioBelaVista cenario, CalculadoraRisco calculadora) {
        LocalDateTime tarde = CenarioBelaVista.TARDE;
        LocalDateTime noite = CenarioBelaVista.NOITE;
        Usuario usuario = cenario.getUsuarios().get(0);

        System.out.println("===== RISCO DOS TRECHOS AS " + tarde.getHour() + "H E AS " + noite.getHour()
                + "H (PERFIL " + usuario.getPerfil().getNome().toUpperCase() + ") =====");
        for (Trecho trecho : cenario.getTrechoRepositorio().listarTodos()) {
            double riscoTarde = calculadora.calcularRisco(trecho, tarde.getHour(), tarde, usuario.getPerfil());
            double riscoNoite = calculadora.calcularRisco(trecho, noite.getHour(), noite, usuario.getPerfil());
            System.out.println(trecho.getNomeRua() + ": " + formatarRisco(riscoTarde) + " as " + tarde.getHour()
                    + "h | " + formatarRisco(riscoNoite) + " as " + noite.getHour() + "h");
            for (String motivo : calculadora.explicarRisco(trecho, noite.getHour(), noite)) {
                System.out.println("   - " + motivo);
            }
        }
        System.out.println();
    }

    /**
     * Imprime as rotas para cada usuario; so o primeiro mostra os motivos de cada rota
     *
     * @param cenario cenario simulado da Bela Vista
     * @param roteirizador servico que avalia e escolhe as rotas
     */
    private static void imprimirRotas(CenarioBelaVista cenario, Roteirizador roteirizador) {
        ArrayList<RotaConsultada> rotas = cenario.getRotas();
        ArrayList<Usuario> usuarios = cenario.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            imprimirRotasDoUsuario(roteirizador, rotas, usuarios.get(i), CenarioBelaVista.NOITE, i == 0);
        }
    }

    /**
     * Avalia as rotas para o perfil do usuario e imprime a mais rapida, a mais segura e a equilibrada
     *
     * @param roteirizador servico que avalia e escolhe as rotas
     * @param rotas rotas candidatas
     * @param usuario usuario que esta consultando as rotas
     * @param agora data e hora da consulta
     * @param mostrarMotivos true para imprimir os motivos de cada rota
     */
    private static void imprimirRotasDoUsuario(Roteirizador roteirizador, ArrayList<RotaConsultada> rotas,
                                               Usuario usuario, LocalDateTime agora, boolean mostrarMotivos) {
        int hora = agora.getHour();
        roteirizador.avaliarRotas(rotas, hora, agora, usuario.getPerfil());

        System.out.println("===== ROTAS TRIANON-MASP ATE RUA TREZE DE MAIO - " + usuario.getApelido().toUpperCase()
                + " (" + usuario.getPerfil().getNome().toUpperCase() + ") AS " + hora + "H =====");
        for (RotaConsultada rota : rotas) {
            System.out.println(Formatacao.barra(rota.getNotaSeguranca(), Roteirizador.NOTA_MAXIMA) + " " + rota
                    + " | custo " + Formatacao.decimal(roteirizador.calcularCusto(rota, ALFA)));
            if (mostrarMotivos) {
                for (String motivo : rota.getMotivos()) {
                    System.out.println("   - " + motivo);
                }
            }
        }
        System.out.println("Mais rapida: " + roteirizador.escolherMaisRapida(rotas).getDescricao());
        System.out.println("Mais segura: " + roteirizador.escolherMaisSegura(rotas).getDescricao());
        System.out.println("Equilibrada (alfa " + Formatacao.decimal(ALFA) + "): "
                + roteirizador.escolherEquilibrada(rotas, ALFA).getDescricao());
        System.out.println();
    }

    /**
     * Imprime a recomendacao final para o primeiro usuario a noite: a rota equilibrada,
     * quanto ela custa a mais que a rota mais rapida e quais trechos evitar
     *
     * @param cenario cenario simulado da Bela Vista
     * @param calculadora calculadora de risco dos trechos
     * @param roteirizador servico que avalia e escolhe as rotas
     */
    private static void imprimirRecomendacao(CenarioBelaVista cenario, CalculadoraRisco calculadora,
                                             Roteirizador roteirizador) {
        Usuario usuario = cenario.getUsuarios().get(0);
        LocalDateTime noite = CenarioBelaVista.NOITE;
        int hora = noite.getHour();
        ArrayList<RotaConsultada> rotas = cenario.getRotas();
        roteirizador.avaliarRotas(rotas, hora, noite, usuario.getPerfil());
        RotaConsultada rapida = roteirizador.escolherMaisRapida(rotas);
        RotaConsultada sugerida = roteirizador.escolherEquilibrada(rotas, ALFA);

        System.out.println("===== RECOMENDACAO PARA " + usuario.getApelido().toUpperCase() + " ("
                + usuario.getPerfil().getNome().toUpperCase() + ") AS " + hora + "H =====");
        System.out.println("Siga pela rota: " + sugerida.getDescricao());
        System.out.print(Formatacao.barra(sugerida.getNotaSeguranca(), Roteirizador.NOTA_MAXIMA) + " nota "
                + Formatacao.decimal(sugerida.getNotaSeguranca()) + "/100");
        if (sugerida == rapida) {
            System.out.println(" | ja e tambem a rota mais rapida");
        } else {
            int metrosAMais = sugerida.calcularDistanciaTotal() - rapida.calcularDistanciaTotal();
            double pontosAMais = sugerida.getNotaSeguranca() - rapida.getNotaSeguranca();
            System.out.println(" | " + metrosAMais + " m a mais que a mais rapida, com "
                    + Formatacao.decimal(pontosAMais) + " pontos a mais de seguranca");
        }

        String evitar = "";
        for (Trecho trecho : cenario.getTrechoRepositorio().listarTodos()) {
            double risco = calculadora.calcularRisco(trecho, hora, noite, usuario.getPerfil());
            if (NivelRisco.classificar(risco) == NivelRisco.ALTO) {
                if (!evitar.isEmpty()) {
                    evitar = evitar + ", ";
                }
                evitar = evitar + trecho.getNomeRua() + " (" + Formatacao.decimal(risco) + ")";
            }
        }
        if (!evitar.isEmpty()) {
            System.out.println("Evite os trechos de risco alto: " + evitar);
        }
        System.out.println("Se sentir perigo no caminho, use o botao Me sinto insegura para achar um Ponto Seguro");
        System.out.println();
    }

    /**
     * Simula o botao "Me sinto insegura" e imprime o Ponto Seguro sugerido
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void imprimirPedidoAjuda(CenarioBelaVista cenario) {
        BuscadorPontoApoio buscador = new BuscadorPontoApoio(cenario.getPontoApoioRepositorio());
        Usuario usuario = cenario.getUsuarios().get(0);
        Trecho trecho = cenario.getTrechoPedidoAjuda();
        int hora = CenarioBelaVista.NOITE.getHour();
        double x = trecho.getPosicaoX();
        double y = trecho.getPosicaoY();

        System.out.println("===== BOTAO ME SINTO INSEGURA (" + hora + "H) =====");
        System.out.println(usuario.getApelido() + " pediu ajuda na " + trecho.getNomeRua()
                + " (x = " + (int) x + " m, y = " + (int) y + " m)");
        System.out.println("Pontos que nao entram na busca:");
        for (String descarte : buscador.explicarDescartes(hora)) {
            System.out.println("   - " + descarte);
        }
        PontoApoio sugerido = buscador.buscarMaisProximo(x, y, hora);
        if (sugerido != null) {
            System.out.println("Ponto Seguro sugerido: " + sugerido.getNome() + " a "
                    + Formatacao.decimal(sugerido.calcularDistancia(x, y)) + " m ("
                    + sugerido.descreverHorario() + ")");
            for (String explicacao : buscador.explicarDesempate(x, y, hora)) {
                System.out.println(explicacao);
            }
        } else {
            System.out.println("Nenhum Ponto Seguro aberto por perto");
        }
        System.out.println("Em emergencia ligue 190 (Policia Militar) ou 180 (Central de Atendimento a Mulher)");
        System.out.println();
    }

    /**
     * Imprime todos os pontos de apoio com a prioridade e se podem ser sugeridos
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void imprimirPontosApoio(CenarioBelaVista cenario) {
        System.out.println("===== PONTOS DE APOIO CADASTRADOS =====");
        for (PontoApoio ponto : cenario.getPontoApoioRepositorio().listarTodos()) {
            String sugestao = "nao";
            if (ponto.podeSerSugerido()) {
                sugestao = "sim";
            }
            System.out.println(ponto + " | prioridade " + ponto.prioridade() + " | pode ser sugerido: " + sugestao);
        }
        System.out.println();
    }

    /**
     * Tenta criar ou alterar objetos com dados invalidos e imprime o erro de cada tentativa
     *
     * @param cenario cenario simulado da Bela Vista
     */
    private static void testarValidacoes(CenarioBelaVista cenario) {
        System.out.println("===== TESTE DE VALIDACAO =====");
        Trecho trecho = cenario.getTrechoRepositorio().buscarPorId(2);
        Usuario lua = cenario.getUsuarios().get(0);
        Usuario girassol = cenario.getUsuarios().get(1);
        ReportComunitario report = new ReportComunitario(10, trecho, TipoOcorrencia.ASSEDIO,
                CenarioBelaVista.NOITE, lua);

        // nota de rota acima de 100
        try {
            cenario.getRotas().get(0).setNotaSeguranca(150);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // gravidade fora de 1 a 5
        try {
            new OcorrenciaOficial(11, trecho, TipoOcorrencia.ASSALTO, CenarioBelaVista.NOITE,
                    CenarioBelaVista.FONTE_SSP, 9);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // autor tentando confirmar o proprio report
        try {
            report.confirmar(lua);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // mesma pessoa confirmando duas vezes
        try {
            report.confirmar(girassol);
            report.confirmar(girassol);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // avaliacao com nota 0
        try {
            new AvaliacaoAcolhimento(6, 0, "Nota invalida");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /**
     * Formata o risco com uma casa decimal e o nivel, como 7,0 (alto)
     *
     * @param risco risco do trecho, de 0 a 10
     * @return risco formatado com o nivel
     */
    private static String formatarRisco(double risco) {
        return Formatacao.decimal(risco) + " (" + NivelRisco.classificar(risco).getDescricao() + ")";
    }
}
