package br.com.fiap.caminhoseguro.app;

import br.com.fiap.caminhoseguro.dominio.APe;
import br.com.fiap.caminhoseguro.dominio.AvaliacaoAcolhimento;
import br.com.fiap.caminhoseguro.dominio.Bairro;
import br.com.fiap.caminhoseguro.dominio.Bicicleta;
import br.com.fiap.caminhoseguro.dominio.EstabelecimentoParceiro;
import br.com.fiap.caminhoseguro.dominio.Ocorrencia;
import br.com.fiap.caminhoseguro.dominio.OcorrenciaOficial;
import br.com.fiap.caminhoseguro.dominio.OrgaoPublico;
import br.com.fiap.caminhoseguro.dominio.PontoApoio;
import br.com.fiap.caminhoseguro.dominio.ReportComunitario;
import br.com.fiap.caminhoseguro.dominio.RotaConsultada;
import br.com.fiap.caminhoseguro.dominio.TransportePublico;
import br.com.fiap.caminhoseguro.dominio.Trecho;
import br.com.fiap.caminhoseguro.dominio.Usuario;
import br.com.fiap.caminhoseguro.enums.Iluminacao;
import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;
import br.com.fiap.caminhoseguro.enums.TipoPontoApoio;
import br.com.fiap.caminhoseguro.repositorio.OcorrenciaRepositorio;
import br.com.fiap.caminhoseguro.repositorio.PontoApoioRepositorio;
import br.com.fiap.caminhoseguro.repositorio.TrechoRepositorio;
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

    private static final String FONTE_SSP = "SSP-SP, dados simulados";

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
        // datas de referencia da consulta: 10/11/2026 as 15h e as 23h
        LocalDateTime agora15 = LocalDateTime.of(2026, 11, 10, 15, 0);
        LocalDateTime agora23 = LocalDateTime.of(2026, 11, 10, 23, 0);

        // ===== 1. BAIRRO, TRECHOS E USUARIOS =====
        Bairro belaVista = new Bairro(1, "Bela Vista", "Sao Paulo");

        // posicoes em metros num plano simulado; a origem (0, 0) e a Estacao Trianon-Masp
        Trecho paulistaAugusta = new Trecho(1, "Av. Paulista (Trianon-Augusta)", belaVista, 450, Iluminacao.BOA, 225, 0);
        Trecho augusta = new Trecho(2, "Rua Augusta", belaVista, 400, Iluminacao.REGULAR, 450, 200);
        Trecho freiCaneca = new Trecho(3, "Rua Frei Caneca", belaVista, 300, Iluminacao.RUIM, 600, 400);
        Trecho alamedaSantos = new Trecho(4, "Alameda Santos", belaVista, 450, Iluminacao.REGULAR, 225, 150);
        Trecho pamplona = new Trecho(5, "Rua Pamplona", belaVista, 400, Iluminacao.REGULAR, 450, 450);
        Trecho paulistaBrigadeiro = new Trecho(6, "Av. Paulista (Trianon-Brigadeiro)", belaVista, 900, Iluminacao.BOA, 450, -50);
        Trecho brigadeiro = new Trecho(7, "Av. Brigadeiro Luis Antonio", belaVista, 550, Iluminacao.BOA, 900, 300);
        Trecho trezeDeMaio = new Trecho(8, "Rua Treze de Maio", belaVista, 250, Iluminacao.REGULAR, 750, 600);

        TrechoRepositorio trechoRepositorio = new TrechoRepositorio();
        trechoRepositorio.adicionar(paulistaAugusta);
        trechoRepositorio.adicionar(augusta);
        trechoRepositorio.adicionar(freiCaneca);
        trechoRepositorio.adicionar(alamedaSantos);
        trechoRepositorio.adicionar(pamplona);
        trechoRepositorio.adicionar(paulistaBrigadeiro);
        trechoRepositorio.adicionar(brigadeiro);
        trechoRepositorio.adicionar(trezeDeMaio);

        // usuarios identificados so por apelido, cada um com um perfil de deslocamento
        Usuario lua = new Usuario(1, "Lua", new APe());
        Usuario girassol = new Usuario(2, "Girassol", new Bicicleta());
        Usuario ipe = new Usuario(3, "Ipe", new TransportePublico());

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(lua);
        usuarios.add(girassol);
        usuarios.add(ipe);

        System.out.println("===== BAIRRO, TRECHOS E USUARIOS =====");
        System.out.println(belaVista);
        for (Trecho trecho : trechoRepositorio.buscarPorBairro(belaVista)) {
            System.out.println(trecho);
        }
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
        System.out.println();

        // ===== 2. OCORRENCIAS OFICIAIS E REPORTS =====
        OcorrenciaRepositorio ocorrenciaRepositorio = new OcorrenciaRepositorio();

        ocorrenciaRepositorio.adicionar(new OcorrenciaOficial(1, paulistaAugusta, TipoOcorrencia.ASSALTO,
                LocalDateTime.of(2026, 10, 28, 21, 30), FONTE_SSP, 2));
        ocorrenciaRepositorio.adicionar(new OcorrenciaOficial(2, pamplona, TipoOcorrencia.LOCAL_DESERTO,
                LocalDateTime.of(2026, 11, 3, 22, 0), FONTE_SSP, 2));
        ocorrenciaRepositorio.adicionar(new OcorrenciaOficial(3, paulistaBrigadeiro, TipoOcorrencia.ASSALTO,
                LocalDateTime.of(2026, 10, 15, 19, 0), FONTE_SSP, 1));
        ocorrenciaRepositorio.adicionar(new OcorrenciaOficial(4, brigadeiro, TipoOcorrencia.ACIDENTE,
                LocalDateTime.of(2026, 11, 5, 18, 15), FONTE_SSP, 3));

        // dois reports recentes de assedio na Rua Augusta, cada um confirmado por duas outras pessoas
        ReportComunitario assedioAugusta1 = new ReportComunitario(5, augusta, TipoOcorrencia.ASSEDIO,
                LocalDateTime.of(2026, 11, 10, 9, 30), lua);
        assedioAugusta1.confirmar(girassol);
        assedioAugusta1.confirmar(ipe);
        ocorrenciaRepositorio.adicionar(assedioAugusta1);

        ReportComunitario assedioAugusta2 = new ReportComunitario(6, augusta, TipoOcorrencia.ASSEDIO,
                LocalDateTime.of(2026, 11, 10, 11, 0), girassol);
        assedioAugusta2.confirmar(lua);
        assedioAugusta2.confirmar(ipe);
        ocorrenciaRepositorio.adicionar(assedioAugusta2);

        // report de rua escura com so uma confirmacao: entra com metade do peso
        ReportComunitario ruaEscura = new ReportComunitario(7, freiCaneca, TipoOcorrencia.RUA_ESCURA,
                LocalDateTime.of(2026, 11, 10, 13, 0), ipe);
        ruaEscura.confirmar(lua);
        ocorrenciaRepositorio.adicionar(ruaEscura);

        // report de dois dias atras: confirmado, mas com fator de tempo 0.5
        ReportComunitario assedioPamplona = new ReportComunitario(8, pamplona, TipoOcorrencia.ASSEDIO,
                LocalDateTime.of(2026, 11, 8, 10, 0), lua);
        assedioPamplona.confirmar(girassol);
        assedioPamplona.confirmar(ipe);
        ocorrenciaRepositorio.adicionar(assedioPamplona);

        // report antigo (9 dias): perdeu peso, fator de tempo 0.2
        ReportComunitario assaltoAntigo = new ReportComunitario(9, alamedaSantos, TipoOcorrencia.ASSALTO,
                LocalDateTime.of(2026, 11, 1, 22, 0), girassol);
        assaltoAntigo.confirmar(lua);
        assaltoAntigo.confirmar(ipe);
        ocorrenciaRepositorio.adicionar(assaltoAntigo);

        System.out.println("===== OCORRENCIAS REGISTRADAS (PESO AS 23H) =====");
        for (Ocorrencia ocorrencia : ocorrenciaRepositorio.listarTodos()) {
            System.out.println(ocorrencia + " -> peso " + formatar(ocorrencia.calcularPeso(agora23)));
        }
        System.out.println("Ocorrencias por trecho:");
        for (Trecho trecho : trechoRepositorio.listarTodos()) {
            System.out.println("   " + trecho.getNomeRua() + ": "
                    + ocorrenciaRepositorio.buscarPorTrecho(trecho).size());
        }
        System.out.println();

        // ===== 3. RISCO DE CADA TRECHO AS 15H E AS 23H =====
        CalculadoraRisco calculadora = new CalculadoraRisco();

        System.out.println("===== RISCO DOS TRECHOS AS 15H E AS 23H (PERFIL A PE) =====");
        for (Trecho trecho : trechoRepositorio.listarTodos()) {
            double risco15 = calculadora.calcularRisco(trecho, 15, agora15, lua.getPerfil());
            double risco23 = calculadora.calcularRisco(trecho, 23, agora23, lua.getPerfil());
            System.out.println(trecho.getNomeRua() + ": " + formatar(risco15) + " as 15h | "
                    + formatar(risco23) + " as 23h");
            for (String motivo : calculadora.explicarRisco(trecho, 23, agora23)) {
                System.out.println("   - " + motivo);
            }
        }
        System.out.println();

        // ===== 4. ROTAS DE TRIANON-MASP ATE RUA TREZE DE MAIO =====
        // as rotas candidatas viriam do algoritmo de DP; aqui sao montadas a mao
        RotaConsultada rotaAugusta = new RotaConsultada(1, "Mais curta pela Rua Augusta e Rua Frei Caneca");
        rotaAugusta.adicionarTrecho(paulistaAugusta);
        rotaAugusta.adicionarTrecho(augusta);
        rotaAugusta.adicionarTrecho(freiCaneca);
        rotaAugusta.adicionarTrecho(trezeDeMaio);

        RotaConsultada rotaBrigadeiro = new RotaConsultada(2, "Pela Av. Paulista e Av. Brigadeiro Luis Antonio");
        rotaBrigadeiro.adicionarTrecho(paulistaBrigadeiro);
        rotaBrigadeiro.adicionarTrecho(brigadeiro);
        rotaBrigadeiro.adicionarTrecho(trezeDeMaio);

        RotaConsultada rotaSantos = new RotaConsultada(3, "Pela Alameda Santos e Rua Pamplona");
        rotaSantos.adicionarTrecho(paulistaAugusta);
        rotaSantos.adicionarTrecho(alamedaSantos);
        rotaSantos.adicionarTrecho(pamplona);
        rotaSantos.adicionarTrecho(trezeDeMaio);

        ArrayList<RotaConsultada> rotas = new ArrayList<RotaConsultada>();
        rotas.add(rotaAugusta);
        rotas.add(rotaBrigadeiro);
        rotas.add(rotaSantos);

        Roteirizador roteirizador = new Roteirizador(calculadora);

        // primeiro a Lua, a pe, com os motivos de cada rota
        imprimirRotas(roteirizador, rotas, lua, agora23, true);

        // repete com os outros perfis: a mesma chamada, mas cada perfil ajusta o risco de um jeito
        imprimirRotas(roteirizador, rotas, girassol, agora23, false);
        imprimirRotas(roteirizador, rotas, ipe, agora23, false);

        // ===== 5. BOTAO "ME SINTO INSEGURA" =====
        PontoApoioRepositorio pontoRepositorio = new PontoApoioRepositorio();
        pontoRepositorio.adicionar(new OrgaoPublico(1, "Delegacia de Policia da Bela Vista (simulada)",
                TipoPontoApoio.DELEGACIA, 1200, 800, 0, 0));
        pontoRepositorio.adicionar(new OrgaoPublico(2, "Base da GCM Paulista (simulada)",
                TipoPontoApoio.BASE_GCM, 750, 450, 0, 0));
        pontoRepositorio.adicionar(new OrgaoPublico(3, "Pronto-socorro da regiao (simulado)",
                TipoPontoApoio.HOSPITAL, 1000, 0, 0, 0));
        pontoRepositorio.adicionar(new OrgaoPublico(4, "UBS da regiao (simulada)",
                TipoPontoApoio.UBS, 300, 600, 7, 19));

        EstabelecimentoParceiro farmacia = new EstabelecimentoParceiro(5, "Farmacia Vida 24h",
                "00.000.000/0001-01", 700, 480, 0, 0, true);
        farmacia.adicionarAvaliacao(new AvaliacaoAcolhimento(1, 5, "Me deixaram esperar o carro por aplicativo"));
        farmacia.adicionarAvaliacao(new AvaliacaoAcolhimento(2, 4, "Atendimento rapido"));

        // padaria verificada, mas fecha as 20h
        EstabelecimentoParceiro padaria = new EstabelecimentoParceiro(6, "Padaria Bom Dia",
                "00.000.000/0001-02", 580, 380, 6, 20, true);

        // bar aberto ate as 2h, mas suspenso por 3 avaliacoes ruins
        EstabelecimentoParceiro bar = new EstabelecimentoParceiro(7, "Bar Esquina Amiga",
                "00.000.000/0001-03", 620, 420, 18, 2, true);
        bar.adicionarAvaliacao(new AvaliacaoAcolhimento(3, 2, "Nao quiseram ajudar"));
        bar.adicionarAvaliacao(new AvaliacaoAcolhimento(4, 1, "Fui ignorada"));
        bar.adicionarAvaliacao(new AvaliacaoAcolhimento(5, 2, ""));

        // cafe aberto 24h, mas ainda nao verificado pela equipe
        EstabelecimentoParceiro cafe = new EstabelecimentoParceiro(8, "Cafe Aconchego",
                "00.000.000/0001-04", 640, 400, 0, 0, false);

        pontoRepositorio.adicionar(farmacia);
        pontoRepositorio.adicionar(padaria);
        pontoRepositorio.adicionar(bar);
        pontoRepositorio.adicionar(cafe);

        BuscadorPontoApoio buscador = new BuscadorPontoApoio(pontoRepositorio);

        // Lua esta na Rua Frei Caneca as 23h e aperta o botao
        double posicaoX = freiCaneca.getPosicaoX();
        double posicaoY = freiCaneca.getPosicaoY();

        System.out.println("===== BOTAO ME SINTO INSEGURA (23H) =====");
        System.out.println(lua.getApelido() + " pediu ajuda na " + freiCaneca.getNomeRua()
                + " (x = " + (int) posicaoX + " m, y = " + (int) posicaoY + " m)");
        System.out.println("Pontos que nao entram na busca:");
        for (String descarte : buscador.explicarDescartes(23)) {
            System.out.println("   - " + descarte);
        }
        PontoApoio sugerido = buscador.buscarMaisProximo(posicaoX, posicaoY, 23);
        if (sugerido != null) {
            System.out.println("Ponto Seguro sugerido: " + sugerido.getNome() + " a "
                    + formatar(sugerido.calcularDistancia(posicaoX, posicaoY)) + " m ("
                    + sugerido.descreverHorario() + ")");
            System.out.println("A Farmacia Vida 24h fica a " + formatar(farmacia.calcularDistancia(posicaoX, posicaoY))
                    + " m: distancia parecida, entao o orgao publico tem preferencia");
        } else {
            System.out.println("Nenhum Ponto Seguro aberto por perto");
        }
        System.out.println("Em emergencia ligue 190 (Policia Militar) ou 180 (Central de Atendimento a Mulher)");
        System.out.println();

        // ===== 6. PONTOS DE APOIO CADASTRADOS =====
        // a lista e do tipo PontoApoio, mas cada objeto responde prioridade() do seu jeito
        ArrayList<PontoApoio> pontos = pontoRepositorio.listarTodos();

        System.out.println("===== PONTOS DE APOIO CADASTRADOS =====");
        for (PontoApoio ponto : pontos) {
            String sugestao = "nao";
            if (ponto.podeSerSugerido()) {
                sugestao = "sim";
            }
            System.out.println(ponto + " | prioridade " + ponto.prioridade() + " | pode ser sugerido: " + sugestao);
        }
        System.out.println();

        // ===== 7. TESTE DE VALIDACAO =====
        System.out.println("===== TESTE DE VALIDACAO =====");

        // nota de rota acima de 100
        try {
            rotaAugusta.setNotaSeguranca(150);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // gravidade fora de 1 a 5
        try {
            new OcorrenciaOficial(10, augusta, TipoOcorrencia.ASSALTO, agora23, FONTE_SSP, 9);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // autor tentando confirmar o proprio report
        try {
            assedioAugusta1.confirmar(lua);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // mesma pessoa confirmando duas vezes
        try {
            ruaEscura.confirmar(lua);
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
     * Avalia as rotas para o perfil do usuario e imprime a mais rapida, a mais segura e a equilibrada
     *
     * @param roteirizador servico que avalia e escolhe as rotas
     * @param rotas rotas candidatas
     * @param usuario usuario que esta consultando as rotas
     * @param agora data e hora da consulta
     * @param mostrarMotivos true para imprimir os motivos de cada rota
     */
    private static void imprimirRotas(Roteirizador roteirizador, ArrayList<RotaConsultada> rotas,
                                      Usuario usuario, LocalDateTime agora, boolean mostrarMotivos) {
        int hora = agora.getHour();
        roteirizador.avaliarRotas(rotas, hora, agora, usuario.getPerfil());

        System.out.println("===== ROTAS TRIANON-MASP ATE RUA TREZE DE MAIO - " + usuario.getApelido().toUpperCase()
                + " (" + usuario.getPerfil().getNome().toUpperCase() + ") AS " + hora + "H =====");
        for (RotaConsultada rota : rotas) {
            System.out.println(rota + " | custo " + formatar(roteirizador.calcularCusto(rota, ALFA)));
            if (mostrarMotivos) {
                for (String motivo : rota.getMotivos()) {
                    System.out.println("   - " + motivo);
                }
            }
        }
        System.out.println("Mais rapida: " + roteirizador.escolherMaisRapida(rotas).getDescricao());
        System.out.println("Mais segura: " + roteirizador.escolherMaisSegura(rotas).getDescricao());
        System.out.println("Equilibrada (alfa " + formatar(ALFA) + "): "
                + roteirizador.escolherEquilibrada(rotas, ALFA).getDescricao());
        System.out.println();
    }

    private static String formatar(double valor) {
        return String.format("%.1f", valor);
    }
}
