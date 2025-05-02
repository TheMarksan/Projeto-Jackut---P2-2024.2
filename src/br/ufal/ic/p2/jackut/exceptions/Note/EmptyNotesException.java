package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar recados
 * mas n�o existem recados dispon�veis.
 * <p>
 * Esta � uma exce��o verificada (checked exception), portanto
 * deve ser declarada ou tratada explicitamente.
 * </p>
 *
 * <p>Casos t�picos que lan�am esta exce��o:</p>
 * <ul>
 *   <li>Tentativa de leitura de recados quando a lista est� vazia</li>
 *   <li>Opera��es que requerem a exist�ncia de pelo menos um recado</li>
 *   <li>Tentativa de processamento de recados inexistentes</li>
 * </ul>
 */
public class EmptyNotesException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o: "N�o h� recados."
     */
    public EmptyNotesException() {
        super("N�o h� recados.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (n�o pode ser nula ou vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public EmptyNotesException(String message) {
        super(message);
    }
}