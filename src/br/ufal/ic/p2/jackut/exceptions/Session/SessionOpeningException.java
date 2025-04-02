/**
 * Exceção lançada quando ocorre um erro ao abrir uma sessão.
 * Pode ser causada por credenciais inválidas ou falhas no sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.exceptions.Session;

public class SessionOpeningException extends Exception {

    /**
     * Construtor da exceção com uma mensagem específica.
     *
     */
    public SessionOpeningException() {
        super("Login ou senha inválidos.");
    }
    /**
     * Construtor da exceção com uma mensagem e uma causa específica.
     *
     * @param message A mensagem de erro detalhando o problema.
     */
    public SessionOpeningException(String message) {
        super(message);
    }
}
