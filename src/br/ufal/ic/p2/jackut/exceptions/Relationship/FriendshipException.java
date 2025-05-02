package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exceção lançada quando ocorrem erros em operações de amizade entre usuários.
 * <p>
 * Esta exceção verificada (checked exception) engloba diversos problemas relacionados
 * ao gerenciamento de amizades no sistema.
 * </p>
 *
 * <p>Padrão de uso recomendado:</p>
 * <pre>
 * try {
 *     // operação de amizade
 * } catch (FriendshipException e) {
 *     // tratamento específico
 * }
 * </pre>
 */
public class FriendshipException extends Exception {

    /**
     * Cria uma exceção básica sem mensagem específica.
     */
    public FriendshipException() {
        super();
    }

    /**
     * Método factory para criar exceção de autoamizade.
     *
     * @return FriendshipException configurada para caso de autoamizade
     */
    public static FriendshipException selfFriendship() {
        return new FriendshipException("Usuário não pode adicionar a si mesmo como amigo.");
    }

    /**
     * Método factory para criar exceção de solicitação pendente.
     *
     * @return FriendshipException configurada para caso de solicitação pendente
     */
    public static FriendshipException pendingFriendRequest() {
        return new FriendshipException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }

    /**
     * Método factory para criar exceção de amizade já existente.
     *
     * @return FriendshipException configurada para caso de amizade duplicada
     */
    public static FriendshipException alreadyFriends() {
        return new FriendshipException("Usuário já está adicionado como amigo.");
    }

    /**
     * Método factory para criar exceção de não amizade.
     *
     * @return FriendshipException configurada para caso de usuários não amigos
     */
    public static FriendshipException notFriends() {
        return new FriendshipException("Usuário não está na sua lista de amigos.");
    }

    /**
     * Construtor privado para criação de exceções com mensagens específicas.
     *
     * @param message Mensagem detalhada do erro (não pode ser nula)
     */
    private FriendshipException(String message) {
        super(message);
    }
}