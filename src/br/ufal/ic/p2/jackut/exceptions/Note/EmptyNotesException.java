package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exceção lançada quando uma operação tenta acessar recados
 * mas não existem recados disponíveis.
 * <p>
 * Esta é uma exceção verificada (checked exception), portanto
 * deve ser declarada ou tratada explicitamente.
 * </p>
 *
 * <p>Casos típicos que lançam esta exceção:</p>
 * <ul>
 *   <li>Tentativa de leitura de recados quando a lista está vazia</li>
 *   <li>Operações que requerem a existência de pelo menos um recado</li>
 *   <li>Tentativa de processamento de recados inexistentes</li>
 * </ul>
 */
public class EmptyNotesException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão: "Não há recados."
     */
    public EmptyNotesException() {
        super("Não há recados.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (não pode ser nula ou vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public EmptyNotesException(String message) {
        super(message);
    }
}