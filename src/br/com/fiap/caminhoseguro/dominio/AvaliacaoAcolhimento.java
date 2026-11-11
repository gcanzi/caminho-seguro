package br.com.fiap.caminhoseguro.dominio;

/**
 * Avaliacao anonima de como um estabelecimento parceiro acolheu quem pediu ajuda
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class AvaliacaoAcolhimento {

    private final int id;
    private int nota;
    private String comentario;

    /**
     * Cria uma avaliacao anonima, sem guardar quem avaliou
     *
     * @param id identificador da avaliacao
     * @param nota nota da avaliacao, de 1 a 5
     * @param comentario comentario da avaliacao
     *
     * @throws IllegalArgumentException se a nota estiver fora de 1 a 5
     */
    public AvaliacaoAcolhimento(int id, int nota, String comentario) {
        this.id = id;
        setNota(nota);
        setComentario(comentario);
    }

    /**
     * Devolve o identificador da avaliacao
     *
     * @return identificador da avaliacao
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve a nota da avaliacao, de 1 a 5
     *
     * @return nota da avaliacao, de 1 a 5
     */
    public int getNota() {
        return nota;
    }

    /**
     * Altera a nota da avaliacao
     *
     * @param nota nota da avaliacao, de 1 a 5
     *
     * @throws IllegalArgumentException se a nota estiver fora de 1 a 5
     */
    public void setNota(int nota) {
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("Nota da avaliacao deve estar entre 1 e 5, recebido: " + nota);
        }
        this.nota = nota;
    }

    /**
     * Devolve o comentario da avaliacao
     *
     * @return comentario da avaliacao
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Altera o comentario; comentario nulo vira texto vazio
     *
     * @param comentario comentario da avaliacao
     */
    public void setComentario(String comentario) {
        if (comentario == null) {
            comentario = "";
        }
        this.comentario = comentario;
    }

    /**
     * Mostra a avaliacao em uma linha
     *
     * @return texto com nota e comentario
     */
    @Override
    public String toString() {
        return "Nota " + nota + " - \"" + comentario + "\"";
    }
}
