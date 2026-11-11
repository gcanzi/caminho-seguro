package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoPontoApoio;

/**
 * Ponto Seguro onde a pessoa pode pedir ajuda, com posicao e horario de funcionamento
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public abstract class PontoApoio {

    private int id;
    private String nome;
    private TipoPontoApoio tipo;
    private double posicaoX;
    private double posicaoY;
    private int horaAbertura;
    private int horaFechamento;

    /**
     * Cria um ponto de apoio; abertura igual ao fechamento significa aberto 24 horas
     *
     * @param id identificador do ponto de apoio
     * @param nome nome do ponto de apoio
     * @param tipo tipo do ponto de apoio
     * @param posicaoX coordenada X do ponto no plano do bairro, em metros
     * @param posicaoY coordenada Y do ponto no plano do bairro, em metros
     * @param horaAbertura hora de abertura, de 0 a 23
     * @param horaFechamento hora de fechamento, de 0 a 23
     *
     * @throws IllegalArgumentException se o nome estiver vazio, o tipo for nulo ou as horas estiverem fora de 0 a 23
     */
    public PontoApoio(int id, String nome, TipoPontoApoio tipo, double posicaoX, double posicaoY,
                      int horaAbertura, int horaFechamento) {
        this.id = id;
        setNome(nome);
        setTipo(tipo);
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        setHoraAbertura(horaAbertura);
        setHoraFechamento(horaFechamento);
    }

    /**
     * Define a prioridade do ponto na sugestao; quanto menor, mais preferido
     *
     * @return prioridade do ponto
     */
    public abstract int prioridade();

    /**
     * Informa se o ponto pode ser sugerido para quem pede ajuda
     *
     * @return true se o ponto pode ser sugerido
     */
    public abstract boolean podeSerSugerido();

    /**
     * Verifica se o ponto esta aberto na hora informada, tratando horario que vira a meia-noite e 24 horas
     *
     * @param hora hora da consulta, de 0 a 23
     * @return true se estiver aberto
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public boolean estaAberto(int hora) {
        Horario.validarHora(hora);
        if (horaAbertura == horaFechamento) {
            // aberto 24 horas
            return true;
        } else if (horaAbertura < horaFechamento) {
            return hora >= horaAbertura && hora < horaFechamento;
        } else {
            // vira a meia-noite, por exemplo das 18h as 2h
            return hora >= horaAbertura || hora < horaFechamento;
        }
    }

    /**
     * Calcula a distancia em linha reta ate uma posicao do plano do bairro
     *
     * @param x coordenada X da pessoa, em metros
     * @param y coordenada Y da pessoa, em metros
     * @return distancia em metros
     */
    public double calcularDistancia(double x, double y) {
        double dx = posicaoX - x;
        double dy = posicaoY - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Descreve o horario de funcionamento
     *
     * @return "aberto 24 horas" ou "das Xh as Yh"
     */
    public String descreverHorario() {
        if (horaAbertura == horaFechamento) {
            return "aberto 24 horas";
        }
        return "das " + horaAbertura + "h as " + horaFechamento + "h";
    }

    /**
     * Devolve o identificador do ponto de apoio
     *
     * @return identificador do ponto de apoio
     */
    public int getId() {
        return id;
    }

    /**
     * Altera o identificador do ponto de apoio
     *
     * @param id novo identificador do ponto de apoio
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devolve o nome do ponto de apoio
     *
     * @return nome do ponto de apoio
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o nome do ponto
     *
     * @param nome nome do ponto de apoio
     *
     * @throws IllegalArgumentException se o nome estiver vazio
     */
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do ponto de apoio nao pode ser vazio");
        }
        this.nome = nome;
    }

    /**
     * Devolve o tipo do ponto de apoio
     *
     * @return tipo do ponto de apoio
     */
    public TipoPontoApoio getTipo() {
        return tipo;
    }

    /**
     * Altera o tipo do ponto
     *
     * @param tipo tipo do ponto de apoio
     *
     * @throws IllegalArgumentException se o tipo for nulo
     */
    public void setTipo(TipoPontoApoio tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo do ponto de apoio nao pode ser nulo");
        }
        this.tipo = tipo;
    }

    /**
     * Devolve a coordenada X do ponto no plano do bairro, em metros
     *
     * @return coordenada X do ponto no plano do bairro, em metros
     */
    public double getPosicaoX() {
        return posicaoX;
    }

    /**
     * Altera a coordenada X do ponto no plano do bairro, em metros
     *
     * @param posicaoX nova coordenada X do ponto no plano do bairro, em metros
     */
    public void setPosicaoX(double posicaoX) {
        this.posicaoX = posicaoX;
    }

    /**
     * Devolve a coordenada Y do ponto no plano do bairro, em metros
     *
     * @return coordenada Y do ponto no plano do bairro, em metros
     */
    public double getPosicaoY() {
        return posicaoY;
    }

    /**
     * Altera a coordenada Y do ponto no plano do bairro, em metros
     *
     * @param posicaoY nova coordenada Y do ponto no plano do bairro, em metros
     */
    public void setPosicaoY(double posicaoY) {
        this.posicaoY = posicaoY;
    }

    /**
     * Devolve a hora de abertura, de 0 a 23
     *
     * @return hora de abertura, de 0 a 23
     */
    public int getHoraAbertura() {
        return horaAbertura;
    }

    /**
     * Altera a hora de abertura
     *
     * @param horaAbertura hora de abertura, de 0 a 23
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public void setHoraAbertura(int horaAbertura) {
        Horario.validarHora(horaAbertura);
        this.horaAbertura = horaAbertura;
    }

    /**
     * Devolve a hora de fechamento, de 0 a 23
     *
     * @return hora de fechamento, de 0 a 23
     */
    public int getHoraFechamento() {
        return horaFechamento;
    }

    /**
     * Altera a hora de fechamento
     *
     * @param horaFechamento hora de fechamento, de 0 a 23
     *
     * @throws IllegalArgumentException se a hora estiver fora de 0 a 23
     */
    public void setHoraFechamento(int horaFechamento) {
        Horario.validarHora(horaFechamento);
        this.horaFechamento = horaFechamento;
    }

    /**
     * Mostra o ponto de apoio em uma linha
     *
     * @return texto com tipo, nome e horario
     */
    @Override
    public String toString() {
        return tipo + " - " + nome + " (" + descreverHorario() + ")";
    }
}
