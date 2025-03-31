/**
 * Exceção lançada quando ocorre um erro ao abrir uma sessão.
 * Pode ser causada por credenciais inválidas ou falhas no sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */


package br.ufal.ic.p2.jackut.exceptions;

public class ProfileEditException extends Exception {
    /**
     * Construtor da exceção com uma mensagem específica.
     *
     * @param message A mensagem de erro detalhando o problema.
     */
    public ProfileEditException(String message) {
        super(message);
    }

    /**
     * Construtor da exceção com uma mensagem e uma causa específica.
     *
     * @param message A mensagem de erro detalhando o problema.
     * @param cause A causa raiz da exceção.
     */
    public ProfileEditException(String message, Throwable cause) {
        super(message, cause);
    }
}
