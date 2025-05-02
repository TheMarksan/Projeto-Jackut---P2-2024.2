package br.ufal.ic.p2.jackut.exceptions.Profile;

/**
 * Exce��o lan�ada quando um atributo de perfil possui valor inv�lido ou n�o existe.
 * <p>
 * Esta � uma exce��o verificada (checked exception) que indica problemas de valida��o
 * ou consist�ncia em atributos do perfil do usu�rio.
 * </p>
 *
 * <p><b>Cen�rios t�picos:</b></p>
 * <ul>
 *   <li>Valor de atributo fora do dom�nio permitido</li>
 *   <li>Tipo de dado incompat�vel com o atributo</li>
 *   <li>Tentativa de acesso a atributo n�o existente</li>
 *   <li>Formato inv�lido para o atributo especificado</li>
 * </ul>
 *
 * <p><b>Sugest�es de tratamento:</b></p>
 * <ol>
 *   <li>Validar os valores antes de atribu�-los</li>
 *   <li>Verificar a documenta��o dos atributos obrigat�rios</li>
 *   <li>Fornecer feedback claro ao usu�rio sobre o erro espec�fico</li>
 *   <li>Implementar tratamento adequado para valores inv�lidos</li>
 * </ol>
 */
public class InvalidAttributeException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o: "Atributo inv�lido."
     */
    public InvalidAttributeException() {
        super("Atributo inv�lido.");
    }

    /**
     * Cria uma exce��o com mensagem espec�fica para o atributo inv�lido.
     *
     * @param attribute Nome do atributo inv�lido (n�o pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se o nome do atributo for nulo ou vazio
     */
    public InvalidAttributeException(String attribute) {
        super("O atributo '" + attribute + "' � inv�lido ou inexistente.");
    }
}