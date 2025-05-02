package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exce��o lan�ada quando ocorrem erros em opera��es de amizade entre usu�rios.
 * <p>
 * Esta exce��o verificada (checked exception) engloba diversos problemas relacionados
 * ao gerenciamento de amizades no sistema.
 * </p>
 *
 * <p>Padr�o de uso recomendado:</p>
 * <pre>
 * try {
 *     // opera��o de amizade
 * } catch (FriendshipException e) {
 *     // tratamento espec�fico
 * }
 * </pre>
 */
public class FriendshipException extends Exception {

    /**
     * Cria uma exce��o b�sica sem mensagem espec�fica.
     */
    public FriendshipException() {
        super();
    }

    /**
     * M�todo factory para criar exce��o de autoamizade.
     *
     * @return FriendshipException configurada para caso de autoamizade
     */
    public static FriendshipException selfFriendship() {
        return new FriendshipException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
    }

    /**
     * M�todo factory para criar exce��o de solicita��o pendente.
     *
     * @return FriendshipException configurada para caso de solicita��o pendente
     */
    public static FriendshipException pendingFriendRequest() {
        return new FriendshipException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }

    /**
     * M�todo factory para criar exce��o de amizade j� existente.
     *
     * @return FriendshipException configurada para caso de amizade duplicada
     */
    public static FriendshipException alreadyFriends() {
        return new FriendshipException("Usu�rio j� est� adicionado como amigo.");
    }

    /**
     * M�todo factory para criar exce��o de n�o amizade.
     *
     * @return FriendshipException configurada para caso de usu�rios n�o amigos
     */
    public static FriendshipException notFriends() {
        return new FriendshipException("Usu�rio n�o est� na sua lista de amigos.");
    }

    /**
     * Construtor privado para cria��o de exce��es com mensagens espec�ficas.
     *
     * @param message Mensagem detalhada do erro (n�o pode ser nula)
     */
    private FriendshipException(String message) {
        super(message);
    }
}