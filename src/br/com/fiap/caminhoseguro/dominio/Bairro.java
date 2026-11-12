package br.com.fiap.caminhoseguro.dominio;

/**
 * Bairro atendido pelo piloto do Caminho Seguro
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Bairro {

    private final int id;
    private String nome;
    private String cidade;

    /**
     * Cria um bairro com nome e cidade
     *
     * @param id identificador do bairro
     * @param nome nome do bairro
     * @param cidade cidade do bairro
     *
     * @throws IllegalArgumentException se o nome ou a cidade estiverem vazios
     */
    public Bairro(int id, String nome, String cidade) {
        this.id = id;
        setNome(nome);
        setCidade(cidade);
    }

    /**
     * Devolve o identificador do bairro
     *
     * @return identificador do bairro
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o nome do bairro
     *
     * @return nome do bairro
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera o nome do bairro
     *
     * @param nome nome do bairro
     *
     * @throws IllegalArgumentException se o nome estiver vazio
     */
    public void setNome(String nome) {
        Validacao.textoObrigatorio(nome, "Nome do bairro nao pode ser vazio");
        this.nome = nome;
    }

    /**
     * Devolve a cidade do bairro
     *
     * @return cidade do bairro
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Altera a cidade do bairro
     *
     * @param cidade cidade do bairro
     *
     * @throws IllegalArgumentException se a cidade estiver vazia
     */
    public void setCidade(String cidade) {
        Validacao.textoObrigatorio(cidade, "Cidade do bairro nao pode ser vazia");
        this.cidade = cidade;
    }

    /**
     * Mostra o bairro em uma linha
     *
     * @return texto com nome e cidade
     */
    @Override
    public String toString() {
        return "Bairro " + id + " - " + nome + ", " + cidade;
    }
}
