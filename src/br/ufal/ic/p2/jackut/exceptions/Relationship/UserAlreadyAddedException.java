package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exceção lançada quando um usuário tenta adicionar um relacionamento que já existe.
 * <p>
 * Esta exceção verificada (checked exception) indica tentativas de duplicação
 * de relacionamentos entre usuários.
 * </p>
 *
 * <p><b>Tipos de relacionamento suportados:</b></p>
 * <ul>
 *   <li>amigo</li>
 *   <li>inimigo</li>
 *   <li>paquera</li>
 *   <li>ídolo</li>
 *   <li>fã</li>
 * </ul>
 *
 * <p><b>Exemplo de tratamento:</b></p>
 * <pre>
 * try {
 *     // tentar adicionar relacionamento
 * } catch (UserAlreadyAddedException e) {
 *     System.out.println("Erro: " + e.getMessage());
 *     // oferecer opção de remover ou modificar
 * }
 * </pre>
 */
public class UserAlreadyAddedException extends Exception {

    /**
     * Cria uma exceção para relacionamento duplicado.
     *
     * @param relationshipType Tipo de relacionamento que já existe (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se relationshipType for nulo ou vazio
     */
    public UserAlreadyAddedException(String relationshipType) {
        super("Usuário já está adicionado como " + relationshipType.toLowerCase() + ".");
    }
}