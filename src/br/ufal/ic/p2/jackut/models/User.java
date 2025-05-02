/**
 * F
 *
 * @author MarcosMelo
 * @version 1.0
 */

package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Classe que representa o usu�rio.
 *
 * <p>Esta classe � serializ�vel para permitir armazenamento e transmiss�o.</p>
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String password;
    private String login;
    private UserProfile profile;  // Perfil associado ao usu�rio


    /**
     * Construtor da classe User.
     *
     * @param name O nome do usu�rio.
     * @param password A senha do usu�rio.
     * @param login O login do usu�rio.
     */
    public User(String name, String password, String login) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.profile = new UserProfile();  // Inicializa o perfil do usu�rio
    }

    /**
     * Obt�m o nome do usu�rio.
     *
     * @return O nome do usu�rio.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Define um novo nome para o usu�rio.
     *
     * @param name O novo nome do usu�rio.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obt�m a senha do usu�rio.
     *
     * @return A senha do usu�rio.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define uma nova senha para o usu�rio.
     *
     * @param password A nova senha do usu�rio.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obt�m o login do usu�rio.
     *
     * @return O login do usu�rio.
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Define um novo login para o usu�rio.
     *
     * @param login O novo login do usu�rio.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Obt�m o perfil do usu�rio.
     *
     * @return O perfil do usu�rio.
     */
    public UserProfile getProfile() {
        return this.profile;
    }

    /**
     * Define um novo perfil para o usu�rio.
     *
     * @param profile O novo perfil do usu�rio.
     */
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
