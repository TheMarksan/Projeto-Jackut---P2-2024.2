package br.ufal.ic.p2.jackut.exceptions.Message;

/**
 * Exceção lançada quando uma operação tenta ler mensagens de um usuário,
 * mas não há mensagens disponíveis.
 * <p>
 * Esta exceção é do tipo {@link RuntimeException}, portanto não requer
 * declaração explícita em métodos que podem lançá-la.
 * </p>
 *
 * <p>Tipicamente ocorre em operações como:</p>
 * <ul>
 *   <li>Tentativa de ler mensagens quando a caixa de entrada está vazia</li>
 *   <li>Operações que dependem da existência de mensagens prévias</li>
 * </ul>
 */
public class EmptyMessagesException extends RuntimeException {

    /**
     * Cria uma exceção com a mensagem padrão: "Não há mensagens."
     */
    public EmptyMessagesException() {
        super("Não há mensagens.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (não pode ser nula)
     */
    public EmptyMessagesException(String message) {
        super(message);
    }
}