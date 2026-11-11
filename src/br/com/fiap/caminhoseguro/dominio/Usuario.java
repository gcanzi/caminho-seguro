package br.com.fiap.caminhoseguro.dominio;

/**
 * Pessoa que usa o app, identificada so por um apelido, sem dados pessoais reais
 *
 * @author Gustavo Canzi
 * @version 1.0
 * @since 1.0
 */
public class Usuario {

    private final int id;
    private String apelido;
    private PerfilDeslocamento perfil;

    /**
     * Cria um usuario com apelido e perfil de deslocamento
     *
     * @param id identificador do usuario
     * @param apelido apelido do usuario, sem dados pessoais
     * @param perfil perfil de deslocamento do usuario
     *
     * @throws IllegalArgumentException se o apelido estiver vazio ou o perfil for nulo
     */
    public Usuario(int id, String apelido, PerfilDeslocamento perfil) {
        this.id = id;
        setApelido(apelido);
        setPerfil(perfil);
    }

    /**
     * Devolve o identificador do usuario
     *
     * @return identificador do usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o apelido do usuario, sem dados pessoais
     *
     * @return apelido do usuario, sem dados pessoais
     */
    public String getApelido() {
        return apelido;
    }

    /**
     * Altera o apelido do usuario
     *
     * @param apelido apelido do usuario, sem dados pessoais
     *
     * @throws IllegalArgumentException se o apelido estiver vazio
     */
    public void setApelido(String apelido) {
        if (apelido == null || apelido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apelido do usuario nao pode ser vazio");
        }
        this.apelido = apelido;
    }

    /**
     * Devolve o perfil de deslocamento do usuario
     *
     * @return perfil de deslocamento do usuario
     */
    public PerfilDeslocamento getPerfil() {
        return perfil;
    }

    /**
     * Altera o perfil de deslocamento do usuario
     *
     * @param perfil perfil de deslocamento do usuario
     *
     * @throws IllegalArgumentException se o perfil for nulo
     */
    public void setPerfil(PerfilDeslocamento perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Perfil de deslocamento nao pode ser nulo");
        }
        this.perfil = perfil;
    }

    /**
     * Mostra o usuario em uma linha
     *
     * @return texto com apelido e perfil
     */
    @Override
    public String toString() {
        return "Usuario " + id + " - " + apelido + " (" + perfil.getNome() + ")";
    }
}
