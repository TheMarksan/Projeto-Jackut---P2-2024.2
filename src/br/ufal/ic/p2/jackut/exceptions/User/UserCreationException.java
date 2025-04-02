/**
 * Exceção lançada quando ocorre um erro ao criar um usuário.
 * Pode ser causada por informações inválidas ou tentativa de criar um usuário já existente.
 *
 * @author SeuNome
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.exceptions.User;

public class UserCreationException extends Exception {

    /**
     * Construtor da exceção com uma mensagem específica.
     *
     * @param message A mensagem de erro detalhando o problema.
     */
    public UserCreationException(String message) {
        super(message);
    }

    /**
     * Construtor da exceção com uma mensagem e uma causa específica.
     *
     * @param message A mensagem de erro detalhando o problema.
     * @param cause A causa raiz da exceção.
     */
    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}