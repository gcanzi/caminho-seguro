package br.com.fiap.caminhoseguro.app;

import br.com.fiap.caminhoseguro.dominio.APe;
import br.com.fiap.caminhoseguro.dominio.AvaliacaoAcolhimento;
import br.com.fiap.caminhoseguro.dominio.Bairro;
import br.com.fiap.caminhoseguro.dominio.Bicicleta;
import br.com.fiap.caminhoseguro.dominio.EstabelecimentoParceiro;
import br.com.fiap.caminhoseguro.dominio.OcorrenciaOficial;
import br.com.fiap.caminhoseguro.dominio.OrgaoPublico;
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

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Dados simulados do piloto na Bela Vista: trechos, usuarios, ocorrencias, rotas e pontos de apoio.
 * Posicoes em metros num plano simulado; a origem (0, 0) e a Estacao Trianon-Masp.
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class CenarioBelaVista {

    /** Data e hora da consulta feita a tarde */
    public static final LocalDateTime TARDE = LocalDateTime.of(2026, 11, 10, 15, 0);
    /** Data e hora da consulta feita a noite */
    public static final LocalDateTime NOITE = LocalDateTime.of(2026, 11, 10, 23, 0);
    /** Fonte usada nas ocorrencias oficiais */
    public static final String FONTE_SSP = "SSP-SP, dados simulados";

    private Bairro bairro;
    private TrechoRepositorio trechoRepositorio;
    private OcorrenciaRepositorio ocorrenciaRepositorio;
    private PontoApoioRepositorio pontoApoioRepositorio;
    private ArrayList<Usuario> usuarios;
    private ArrayList<RotaConsultada> rotas;

    private Trecho paulistaAugusta;
    private Trecho augusta;
    private Trecho freiCaneca;
    private Trecho alamedaSantos;
    private Trecho pamplona;
    private Trecho paulistaBrigadeiro;
    private Trecho brigadeiro;
    private Trecho trezeDeMaio;

    /**
     * Monta todo o cenario simulado
     */
    public CenarioBelaVista() {
        bairro = new Bairro(1, "Bela Vista", "Sao Paulo");
        montarTrechos();
        montarUsuarios();
        montarOcorrencias();
        montarRotas();
        montarPontosApoio();
    }

    private void montarTrechos() {
        paulistaAugusta = new Trecho(1, "Av. Paulista (Trianon-Augusta)", bairro, 450, Iluminacao.BOA, 225, 0);
        augusta = new Trecho(2, "Rua Augusta", bairro, 400, Iluminacao.REGULAR, 450, 200);
        freiCaneca = new Trecho(3, "Rua Frei Caneca", bairro, 300, Iluminacao.RUIM, 600, 400);
        alamedaSantos = new Trecho(4, "Alameda Santos", bairro, 450, Iluminacao.REGULAR, 225, 150);
        pamplona = new Trecho(5, "Rua Pamplona", bairro, 400, Iluminacao.REGULAR, 450, 450);
        paulistaBrigadeiro = new Trecho(6, "Av. Paulista (Trianon-Brigadeiro)", bairro, 900, Iluminacao.BOA, 450, -50);
        brigadeiro = new Trecho(7, "Av. Brigadeiro Luis Antonio", bairro, 550, Iluminacao.BOA, 900, 300);
        trezeDeMaio = new Trecho(8, "Rua Treze de Maio", bairro, 250, Iluminacao.REGULAR, 750, 600);

        trechoRepositorio = new TrechoRepositorio();
        trechoRepositorio.adicionar(paulistaAugusta);
        trechoRepositorio.adicionar(augusta);
        trechoRepositorio.adicionar(freiCaneca);
        trechoRepositorio.adicionar(alamedaSantos);
        trechoRepositorio.adicionar(pamplona);
        trechoRepositorio.adicionar(paulistaBrigadeiro);
        trechoRepositorio.adicionar(brigadeiro);
        trechoRepositorio.adicionar(trezeDeMaio);
    }

    private void montarUsuarios() {
        // usuarios identificados so por apelido, cada um com um perfil de deslocamento
        usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario(1, "Lua", new APe()));
        usuarios.add(new Usuario(2, "Girassol", new Bicicleta()));
        usuarios.add(new Usuario(3, "Ipe", new TransportePublico()));
    }

    private void montarOcorrencias() {
        Usuario lua = usuarios.get(0);
        Usuario girassol = usuarios.get(1);
        Usuario ipe = usuarios.get(2);
        ocorrenciaRepositorio = new OcorrenciaRepositorio();

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
    }

    private void montarRotas() {
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

        rotas = new ArrayList<RotaConsultada>();
        rotas.add(rotaAugusta);
        rotas.add(rotaBrigadeiro);
        rotas.add(rotaSantos);
    }

    private void montarPontosApoio() {
        pontoApoioRepositorio = new PontoApoioRepositorio();
        pontoApoioRepositorio.adicionar(new OrgaoPublico(1, "Delegacia de Policia da Bela Vista (simulada)",
                TipoPontoApoio.DELEGACIA, 1200, 800, 0, 0));
        pontoApoioRepositorio.adicionar(new OrgaoPublico(2, "Base da GCM Paulista (simulada)",
                TipoPontoApoio.BASE_GCM, 750, 450, 0, 0));
        pontoApoioRepositorio.adicionar(new OrgaoPublico(3, "Pronto-socorro da regiao (simulado)",
                TipoPontoApoio.HOSPITAL, 1000, 0, 0, 0));
        pontoApoioRepositorio.adicionar(new OrgaoPublico(4, "UBS da regiao (simulada)",
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

        pontoApoioRepositorio.adicionar(farmacia);
        pontoApoioRepositorio.adicionar(padaria);
        pontoApoioRepositorio.adicionar(bar);
        pontoApoioRepositorio.adicionar(cafe);
    }

    /**
     * Devolve o bairro do piloto
     *
     * @return bairro Bela Vista
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * Devolve o repositorio com os trechos do bairro
     *
     * @return repositorio de trechos
     */
    public TrechoRepositorio getTrechoRepositorio() {
        return trechoRepositorio;
    }

    /**
     * Devolve o repositorio com as ocorrencias oficiais e os reports
     *
     * @return repositorio de ocorrencias
     */
    public OcorrenciaRepositorio getOcorrenciaRepositorio() {
        return ocorrenciaRepositorio;
    }

    /**
     * Devolve o repositorio com os orgaos publicos e estabelecimentos parceiros
     *
     * @return repositorio de pontos de apoio
     */
    public PontoApoioRepositorio getPontoApoioRepositorio() {
        return pontoApoioRepositorio;
    }

    /**
     * Devolve uma copia dos usuarios do cenario: Lua (a pe), Girassol (bicicleta) e Ipe (transporte publico)
     *
     * @return copia da lista de usuarios
     */
    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<Usuario>(usuarios);
    }

    /**
     * Devolve uma copia das rotas candidatas de Trianon-Masp ate a Rua Treze de Maio
     *
     * @return copia da lista de rotas
     */
    public ArrayList<RotaConsultada> getRotas() {
        return new ArrayList<RotaConsultada>(rotas);
    }

    /**
     * Devolve o trecho onde a pessoa aperta o botao "Me sinto insegura"
     *
     * @return trecho da Rua Frei Caneca
     */
    public Trecho getTrechoPedidoAjuda() {
        return freiCaneca;
    }
}
