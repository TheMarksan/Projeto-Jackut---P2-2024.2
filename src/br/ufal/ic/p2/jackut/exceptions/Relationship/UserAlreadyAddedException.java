package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exce��o lan�ada quando um usu�rio tenta adicionar um relacionamento que j� existe.
 * <p>
 * Esta exce��o verificada (checked exception) indica tentativas de duplica��o
 * de relacionamentos entre usu�rios.
 * </p>
 *
 * <p><b>Tipos de relacionamento suportados:</b></p>
 * <ul>
 *   <li>amigo</li>
 *   <li>inimigo</li>
 *   <li>paquera</li>
 *   <li>�dolo</li>
 *   <li>f�</li>
 * </ul>
 *
 * <p><b>Exemplo de tratamento:</b></p>
 * <pre>
 * try {
 *     // tentar adicionar relacionamento
 * } catch (UserAlreadyAddedException e) {
 *     System.out.println("Erro: " + e.getMessage());
 *     // oferecer op��o de remover ou modificar
 * }
 * </pre>
 */
public class UserAlreadyAddedException extends Exception {

    /**
     * Cria uma exce��o para relacionamento duplicado.
     *
     * @param relationshipType Tipo de relacionamento que j� existe (n�o pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se relationshipType for nulo ou vazio
     */
    public UserAlreadyAddedException(String relationshipType) {
        super("Usu�rio j� est� adicionado como " + relationshipType.toLowerCase() + ".");
    }
}