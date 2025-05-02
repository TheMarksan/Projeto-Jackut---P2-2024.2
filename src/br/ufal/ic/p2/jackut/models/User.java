/**
 * F
 *
 * @author MarcosMelo
 * @version 1.0
 */

package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Classe que representa o usuário.
 *
 * <p>Esta classe é serializável para permitir armazenamento e transmissão.</p>
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String password;
    private String login;
    private UserProfile profile;  // Perfil associado ao usuário


    /**
     * Construtor da classe User.
     *
     * @param name O nome do usuário.
     * @param password A senha do usuário.
     * @param login O login do usuário.
     */
    public User(String name, String password, String login) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.profile = new UserProfile();  // Inicializa o perfil do usuário
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Define um novo nome para o usuário.
     *
     * @param name O novo nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define uma nova senha para o usuário.
     *
     * @param password A nova senha do usuário.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém o login do usuário.
     *
     * @return O login do usuário.
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Define um novo login para o usuário.
     *
     * @param login O novo login do usuário.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Obtém o perfil do usuário.
     *
     * @return O perfil do usuário.
     */
    public UserProfile getProfile() {
        return this.profile;
    }

    /**
     * Define um novo perfil para o usuário.
     *
     * @param profile O novo perfil do usuário.
     */
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
