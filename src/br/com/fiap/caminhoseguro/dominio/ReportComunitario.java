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
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario da confirmacao não pode ser nulo");
        }
        if (usuario.getId() == autor.getId()) {
            throw new IllegalArgumentException("O autor não pode confirmar o proprio report");
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
        if (horas <= 24) {
            return 1.0;
        } else if (horas <= 72) {
            return 0.5;
        } else {
            return 0.2;
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
            peso = peso * 0.5;
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
    public void setAutor(Usuario autor) {
        if (autor == null) {
            throw new IllegalArgumentException("Autor do report nao pode ser nulo");
        }
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

    /**
     * Devolve uma copia da lista de quem confirmou o report
     *
     * @return copia das confirmacoes
     */
    public ArrayList<Usuario> getConfirmacoes() {
        return new ArrayList<Usuario>(confirmacoes);
    }
}
