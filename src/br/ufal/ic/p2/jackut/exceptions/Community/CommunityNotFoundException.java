package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar uma comunidade que n�o existe no sistema.
 * <p>
 * Pode ocorrer em opera��es como:
 * </p>
 * <ul>
 *   <li>Busca por uma comunidade</li>
 *   <li>Tentativa de adicionar membros</li>
 *   <li>Envio de mensagens para comunidades</li>
 * </ul>
 */
public class CommunityNotFoundException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o: "Comunidade n�o existe."
     */
    public CommunityNotFoundException() {
        super("Comunidade n�o existe.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (n�o pode ser nula)
     */
    public CommunityNotFoundException(String message) {
        super(message);
    }
}