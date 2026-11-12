package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoOcorrencia;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Report feito por uma pessoa da comunidade, que ganha forca quando outras pessoas confirmam
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class ReportComunitario extends Ocorrencia {

    /** Quantidade minima de confirmacoes para o report ter peso cheio */
    public static final int MINIMO_CONFIRMACOES = 2;
    /** Ate quantas horas o report e considerado recente */
    public static final int HORAS_RECENTE = 24;
    /** Ate quantas horas o report ainda tem peso intermediario */
    public static final int HORAS_INTERMEDIARIO = 72;
    /** Fator de tempo de um report recente */
    public static final double FATOR_RECENTE = 1.0;
    /** Fator de tempo de um report intermediario */
    public static final double FATOR_INTERMEDIARIO = 0.5;
    /** Fator de tempo de um report antigo */
    public static final double FATOR_ANTIGO = 0.2;
    /** Multiplicador do peso enquanto o report nao tem o minimo de confirmacoes */
    public static final double FATOR_SEM_CONFIRMACAO = 0.5;

    private Usuario autor;
    private ArrayList<Usuario> confirmacoes;

    /**
     * Cria um report da comunidade ainda sem confirmacoes
     *
     * @param id identificador do report
     * @param trecho trecho onde a ocorrencia aconteceu
     * @param tipo tipo da ocorrencia relatada
     * @param dataHora data e hora do report
     * @param autor usuario que fez o report
     *
     * @throws IllegalArgumentException se algum dado obrigatorio for nulo
     */
    public ReportComunitario(int id, Trecho trecho, TipoOcorrencia tipo, LocalDateTime dataHora, Usuario autor) {
        super(id, trecho, tipo, dataHora);
        setAutor(autor);
        this.confirmacoes = new ArrayList<Usuario>();
    }

    /**
     * Registra a confirmacao do report por outra pessoa
     *
     * @param usuario usuario que esta confirmando o report
     *
     * @throws IllegalArgumentException se o usuario for nulo, for o autor do report ou ja tiver confirmado
     */
    public void confirmar(Usuario usuario) {
        Validacao.obrigatorio(usuario, "Usuario da confirmacao nao pode ser nulo");
        if (usuario.getId() == autor.getId()) {
            throw new IllegalArgumentException("O autor nao pode confirmar o proprio report");
        }
        for (Usuario confirmacao : confirmacoes) {
            if (confirmacao.getId() == usuario.getId()) {
                throw new IllegalArgumentException(usuario.getApelido() + " ja confirmou o report " + getId());
            }
        }
        confirmacoes.add(usuario);
    }

    /**
     * Verifica se o report ja tem o minimo de confirmacoes
     *
     * @return true se tiver pelo menos 2 confirmacoes
     */
    public boolean estaConfirmado() {
        return confirmacoes.size() >= MINIMO_CONFIRMACOES;
    }

    /**
     * Calcula o fator de tempo: reports antigos perdem peso
     *
     * @param agora data e hora de referencia da consulta
     * @return 1.0 ate 24 h, 0.5 ate 72 h e 0.2 depois disso
     */
    public double calcularFatorTempo(LocalDateTime agora) {
        long horas = calcularHorasDesde(agora);
        if (horas <= HORAS_RECENTE) {
            return FATOR_RECENTE;
        } else if (horas <= HORAS_INTERMEDIARIO) {
            return FATOR_INTERMEDIARIO;
        } else {
            return FATOR_ANTIGO;
        }
    }

    /**
     * Calcula o peso do report: peso do tipo x fator de tempo, pela metade se ainda nao foi confirmado
     *
     * @param agora data e hora de referencia da consulta
     * @return peso do report no risco do trecho
     */
    @Override
    public double calcularPeso(LocalDateTime agora) {
        double peso = getTipo().getPeso() * calcularFatorTempo(agora);
        if (!estaConfirmado()) {
            peso = peso * FATOR_SEM_CONFIRMACAO;
        }
        return peso;
    }

    /**
     * Descreve o report com o autor e as confirmacoes
     *
     * @return texto do report
     */
    @Override
    public String descrever() {
        String textoConfirmacoes = confirmacoes.size() + " confirmacoes";
        if (confirmacoes.size() == 1) {
            textoConfirmacoes = "1 confirmacao";
        }
        return "report de " + super.descrever() + " (autor " + autor.getApelido() + ", " + textoConfirmacoes + ")";
    }

    /**
     * Devolve o usuario que fez o report
     *
     * @return usuario que fez o report
     */
    public Usuario getAutor() {
        return autor;
    }

    /**
     * Altera o autor do report
     *
     * @param autor usuario que fez o report
     *
     * @throws IllegalArgumentException se o autor for nulo
     */
    private void setAutor(Usuario autor) {
        Validacao.obrigatorio(autor, "Autor do report nao pode ser nulo");
        this.autor = autor;
    }

    /**
     * Devolve a quantidade de pessoas que confirmaram o report
     *
     * @return quantidade de pessoas que confirmaram o report
     */
    public int getQuantidadeConfirmacoes() {
        return confirmacoes.size();
    }

}
