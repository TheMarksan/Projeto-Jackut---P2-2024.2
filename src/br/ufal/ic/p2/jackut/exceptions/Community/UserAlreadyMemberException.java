package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exceção lançada quando um usuário já é membro de uma comunidade
 * e tenta se cadastrar novamente.
 * <p>
 * Esta exceção é do tipo {@link RuntimeException}, portanto não requer
 * declaração explícita em métodos que podem lançá-la.
 * </p>
 */
public class UserAlreadyMemberException extends RuntimeException {

    /**
     * Cria uma exceção com a mensagem padrão:
     * "Usuário já faz parte dessa comunidade."
     */
    public UserAlreadyMemberException() {
        super("Usuario já faz parte dessa comunidade.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (não pode ser nula)
     */
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}