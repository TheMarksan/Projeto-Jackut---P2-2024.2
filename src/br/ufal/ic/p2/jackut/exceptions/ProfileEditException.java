/**
 * Exce��o lan�ada quando ocorre um erro ao abrir uma sess�o.
 * Pode ser causada por credenciais inv�lidas ou falhas no sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */


package br.ufal.ic.p2.jackut.exceptions;

public class ProfileEditException extends Exception {
    /**
     * Construtor da exce��o com uma mensagem espec�fica.
     *
     * @param message A mensagem de erro detalhando o problema.
     */
    public ProfileEditException(String message) {
        super(message);
    }

    /**
     * Construtor da exce��o com uma mensagem e uma causa espec�fica.
     *
     * @param message A mensagem de erro detalhando o problema.
     * @param cause A causa raiz da exce��o.
     */
    public ProfileEditException(String message, Throwable cause) {
        super(message, cause);
    }
}
