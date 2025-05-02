package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exce��o lan�ada quando um usu�rio j� � membro de uma comunidade
 * e tenta se cadastrar novamente.
 * <p>
 * Esta exce��o � do tipo {@link RuntimeException}, portanto n�o requer
 * declara��o expl�cita em m�todos que podem lan��-la.
 * </p>
 */
public class UserAlreadyMemberException extends RuntimeException {

    /**
     * Cria uma exce��o com a mensagem padr�o:
     * "Usu�rio j� faz parte dessa comunidade."
     */
    public UserAlreadyMemberException() {
        super("Usuario j� faz parte dessa comunidade.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (n�o pode ser nula)
     */
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}