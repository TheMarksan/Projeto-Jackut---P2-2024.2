package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exceção lançada quando um usuário tenta criar um relacionamento consigo mesmo.
 * <p>
 * Esta exceção verificada (checked exception) previne operações inválidas onde
 * um usuário tenta estabelecer qualquer tipo de relacionamento consigo próprio.
 * </p>
 *
 * <p><b>Tipos de relacionamento suportados:</b></p>
 * <ul>
 *   <li>Amizade</li>
 *   <li>Inimizade</li>
 *   <li>Paquera</li>
 *   <li>Ídolo/Fã</li>
 * </ul>
 *
 * <p><b>Exemplo de uso:</b></p>
 * <pre>
 * if (usuario.equals(outroUsuario)) {
 *     throw new SelfRelatioshipException("amizade");
 * }
 * </pre>
 */
public class SelfRelationshipException extends Exception {

    /**
     * Cria uma exceção para relacionamento auto-referenciado.
     *
     * @param relationshipType Tipo de relacionamento inválido (não pode ser nulo)
     * @throws IllegalArgumentException Se relationshipType for nulo
     */
    public SelfRelationshipException(String relationshipType) {
        super(
                relationshipType.toLowerCase().equals("amizade") ?
                        "Usuário não pode adicionar a si mesmo como amigo." :
                        "Usuário não pode ser " + relationshipType.toLowerCase() + " de si mesmo."
        );
    }
}