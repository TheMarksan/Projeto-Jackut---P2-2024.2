package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exce��o lan�ada quando um usu�rio tenta criar um relacionamento consigo mesmo.
 * <p>
 * Esta exce��o verificada (checked exception) previne opera��es inv�lidas onde
 * um usu�rio tenta estabelecer qualquer tipo de relacionamento consigo pr�prio.
 * </p>
 *
 * <p><b>Tipos de relacionamento suportados:</b></p>
 * <ul>
 *   <li>Amizade</li>
 *   <li>Inimizade</li>
 *   <li>Paquera</li>
 *   <li>�dolo/F�</li>
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
     * Cria uma exce��o para relacionamento auto-referenciado.
     *
     * @param relationshipType Tipo de relacionamento inv�lido (n�o pode ser nulo)
     * @throws IllegalArgumentException Se relationshipType for nulo
     */
    public SelfRelationshipException(String relationshipType) {
        super(
                relationshipType.toLowerCase().equals("amizade") ?
                        "Usu�rio n�o pode adicionar a si mesmo como amigo." :
                        "Usu�rio n�o pode ser " + relationshipType.toLowerCase() + " de si mesmo."
        );
    }
}