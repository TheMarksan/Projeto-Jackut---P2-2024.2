package br.ufal.ic.p2.jackut.exceptions.Community;

/**
 * Exceção lançada quando uma operação tenta acessar uma comunidade que não existe no sistema.
 * <p>
 * Pode ocorrer em operações como:
 * </p>
 * <ul>
 *   <li>Busca por uma comunidade</li>
 *   <li>Tentativa de adicionar membros</li>
 *   <li>Envio de mensagens para comunidades</li>
 * </ul>
 */
public class CommunityNotFoundException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão: "Comunidade não existe."
     */
    public CommunityNotFoundException() {
        super("Comunidade não existe.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (não pode ser nula)
     */
    public CommunityNotFoundException(String message) {
        super(message);
    }
}