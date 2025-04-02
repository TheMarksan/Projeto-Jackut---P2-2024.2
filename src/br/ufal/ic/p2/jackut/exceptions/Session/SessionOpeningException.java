/**
 * Exce��o lan�ada quando ocorre um erro ao abrir uma sess�o.
 * Pode ser causada por credenciais inv�lidas ou falhas no sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.exceptions.Session;

public class SessionOpeningException extends Exception {

    /**
     * Construtor da exce��o com uma mensagem espec�fica.
     *
     */
    public SessionOpeningException() {
        super("Login ou senha inv�lidos.");
    }
    /**
     * Construtor da exce��o com uma mensagem e uma causa espec�fica.
     *
     * @param message A mensagem de erro detalhando o problema.
     */
    public SessionOpeningException(String message) {
        super(message);
    }
}
