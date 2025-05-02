package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exceção lançada quando ocorre uma falha na criação de uma comunidade.
 * <p>
 * Normalmente ocorre quando já existe uma comunidade com o mesmo nome.
 * </p>
 */
public class CommunityCreationException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão indicando que já existe uma comunidade com o nome informado.
     */
    public CommunityCreationException() {
        super("Comunidade com esse nome já existe.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhando o motivo da exceção
     */
    public CommunityCreationException(String message) {
        super(message);
    }
}