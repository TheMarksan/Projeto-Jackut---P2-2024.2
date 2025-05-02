package br.ufal.ic.p2.jackut.exceptions.Message;

/**
 * Exce��o lan�ada quando uma opera��o tenta ler mensagens de um usu�rio,
 * mas n�o h� mensagens dispon�veis.
 * <p>
 * Esta exce��o � do tipo {@link RuntimeException}, portanto n�o requer
 * declara��o expl�cita em m�todos que podem lan��-la.
 * </p>
 *
 * <p>Tipicamente ocorre em opera��es como:</p>
 * <ul>
 *   <li>Tentativa de ler mensagens quando a caixa de entrada est� vazia</li>
 *   <li>Opera��es que dependem da exist�ncia de mensagens pr�vias</li>
 * </ul>
 */
public class EmptyMessagesException extends RuntimeException {

    /**
     * Cria uma exce��o com a mensagem padr�o: "N�o h� mensagens."
     */
    public EmptyMessagesException() {
        super("N�o h� mensagens.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (n�o pode ser nula)
     */
    public EmptyMessagesException(String message) {
        super(message);
    }
}