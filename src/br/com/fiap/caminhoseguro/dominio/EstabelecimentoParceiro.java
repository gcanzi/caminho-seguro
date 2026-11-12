package br.com.fiap.caminhoseguro.dominio;

import br.com.fiap.caminhoseguro.enums.TipoPontoApoio;

import java.util.ArrayList;

/**
 * Comercio parceiro que acolhe quem se sente em risco; precisa ser verificado e bem avaliado
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class EstabelecimentoParceiro extends PontoApoio {

    /** Quantidade minima de avaliacoes para o ponto poder ser suspenso */
    public static final int MINIMO_AVALIACOES_SUSPENSAO = 3;
    /** Media de avaliacoes abaixo da qual o ponto e suspenso */
    public static final double MEDIA_MINIMA = 3.0;

    private String cnpj;
    private boolean verificado;
    private ArrayList<AvaliacaoAcolhimento> avaliacoes;

    /**
     * Cria um estabelecimento parceiro do tipo COMERCIO, ainda sem avaliacoes
     *
     * @param id identificador do ponto de apoio
     * @param nome nome do ponto de apoio
     * @param cnpj CNPJ ficticio do estabelecimento
     * @param posicaoX coordenada X do ponto no plano do bairro, em metros
     * @param posicaoY coordenada Y do ponto no plano do bairro, em metros
     * @param horaAbertura hora de abertura, de 0 a 23
     * @param horaFechamento hora de fechamento, de 0 a 23
     * @param verificado se o estabelecimento foi verificado pela equipe do app
     *
     * @throws IllegalArgumentException se o CNPJ nao estiver no formato 00.000.000/0000-00 ou algum dado for invalido
     */
    public EstabelecimentoParceiro(int id, String nome, String cnpj, double posicaoX, double posicaoY,
                                   int horaAbertura, int horaFechamento, boolean verificado) {
        super(id, nome, TipoPontoApoio.COMERCIO, posicaoX, posicaoY, horaAbertura, horaFechamento);
        setCnpj(cnpj);
        this.verificado = verificado;
        this.avaliacoes = new ArrayList<AvaliacaoAcolhimento>();
    }

    /**
     * Registra uma avaliacao de acolhimento
     *
     * @param avaliacao avaliacao de acolhimento recebida
     *
     * @throws IllegalArgumentException se a avaliacao for nula
     */
    public void adicionarAvaliacao(AvaliacaoAcolhimento avaliacao) {
        Validacao.obrigatorio(avaliacao, "Avaliacao nao pode ser nula");
        avaliacoes.add(avaliacao);
    }

    /**
     * Calcula a media das notas de acolhimento
     *
     * @return media das notas, ou 0 se nao houver avaliacoes
     */
    public double calcularMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) {
            return 0;
        }
        int soma = 0;
        for (AvaliacaoAcolhimento avaliacao : avaliacoes) {
            soma = soma + avaliacao.getNota();
        }
        return (double) soma / avaliacoes.size();
    }

    /**
     * Verifica se o ponto esta suspenso por avaliacoes ruins
     *
     * @return true se tiver 3 ou mais avaliacoes com media abaixo de 3
     */
    public boolean estaSuspenso() {
        return avaliacoes.size() >= MINIMO_AVALIACOES_SUSPENSAO && calcularMediaAvaliacoes() < MEDIA_MINIMA;
    }

    /**
     * Descreve a situacao do parceiro: verificado, nao verificado ou suspenso
     *
     * @return texto da situacao
     */
    public String descreverSituacao() {
        if (!verificado) {
            return "nao verificado";
        } else if (estaSuspenso()) {
            return "suspenso (media " + String.format("%.1f", calcularMediaAvaliacoes()) + " em "
                    + avaliacoes.size() + " avaliacoes)";
        } else {
            return "verificado";
        }
    }

    /**
     * Comercio parceiro vem depois dos orgaos publicos
     *
     * @return 2
     */
    @Override
    public int prioridade() {
        return 2;
    }

    /**
     * So pode ser sugerido se estiver verificado e nao estiver suspenso
     *
     * @return true se pode ser sugerido
     */
    @Override
    public boolean podeSerSugerido() {
        return verificado && !estaSuspenso();
    }

    /**
     * Devolve o CNPJ ficticio do estabelecimento
     *
     * @return CNPJ ficticio do estabelecimento
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * Altera o CNPJ; no projeto so usamos CNPJ ficticio
     *
     * @param cnpj CNPJ ficticio do estabelecimento
     *
     * @throws IllegalArgumentException se o CNPJ nao tiver 18 caracteres no formato 00.000.000/0000-00
     */
    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 18 || cnpj.charAt(2) != '.' || cnpj.charAt(6) != '.'
                || cnpj.charAt(10) != '/' || cnpj.charAt(15) != '-') {
            throw new IllegalArgumentException("CNPJ deve estar no formato 00.000.000/0000-00, recebido: " + cnpj);
        }
        this.cnpj = cnpj;
    }

    /**
     * Informa se o estabelecimento foi verificado pela equipe do app
     *
     * @return true se o estabelecimento foi verificado pela equipe do app
     */
    public boolean isVerificado() {
        return verificado;
    }

    /**
     * Marca se o estabelecimento foi verificado pela equipe do app
     *
     * @param verificado true se o estabelecimento foi verificado pela equipe do app
     */
    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * Devolve uma copia das avaliacoes do parceiro
     *
     * @return copia das avaliacoes
     */
    public ArrayList<AvaliacaoAcolhimento> getAvaliacoes() {
        return new ArrayList<AvaliacaoAcolhimento>(avaliacoes);
    }

    /**
     * Mostra o parceiro em uma linha
     *
     * @return texto do ponto com CNPJ e situacao
     */
    @Override
    public String toString() {
        return super.toString() + " | CNPJ " + cnpj + ", " + descreverSituacao();
    }
}
