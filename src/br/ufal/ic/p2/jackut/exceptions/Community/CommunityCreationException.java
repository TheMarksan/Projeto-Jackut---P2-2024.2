package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exce��o lan�ada quando ocorre uma falha na cria��o de uma comunidade.
 * <p>
 * Normalmente ocorre quando j� existe uma comunidade com o mesmo nome.
 * </p>
 */
public class CommunityCreationException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o indicando que j� existe uma comunidade com o nome informado.
     */
    public CommunityCreationException() {
        super("Comunidade com esse nome j� existe.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhando o motivo da exce��o
     */
    public CommunityCreationException(String message) {
        super(message);
    }
}