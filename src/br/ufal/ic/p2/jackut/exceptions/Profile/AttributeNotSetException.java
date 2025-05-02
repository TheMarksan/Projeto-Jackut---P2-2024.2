package br.ufal.ic.p2.jackut.exceptions.Profile;

/**
 * Exce��o lan�ada quando um atributo obrigat�rio do perfil n�o foi preenchido.
 * <p>
 * Esta exce��o verificada (checked exception) indica a aus�ncia de valores
 * obrigat�rios em campos do perfil do usu�rio.
 * </p>
 *
 * <p><b>Contextos comuns:</b></p>
 * <ul>
 *   <li>Tentativa de acesso a atributos n�o inicializados</li>
 *   <li>Valida��o de perfis incompletos</li>
 *   <li>Opera��es que requerem atributos espec�ficos</li>
 * </ul>
 *
 * <p><b>Como resolver:</b></p>
 * <ol>
 *   <li>Verificar quais atributos s�o obrigat�rios</li>
 *   <li>Solicitar ao usu�rio o preenchimento dos campos faltantes</li>
 *   <li>Validar o perfil antes de opera��es cr�ticas</li>
 * </ol>
 */
public class AttributeNotSetException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o: "Atributo n�o preenchido."
     */
    public AttributeNotSetException() {
        super("Atributo n�o preenchido.");
    }

    /**
     * Cria uma exce��o com mensagem espec�fica para o atributo faltante.
     *
     * @param attribute Nome do atributo n�o preenchido (n�o pode ser nulo)
     * @throws IllegalArgumentException Se o nome do atributo for nulo
     */
    public AttributeNotSetException(String attribute) {
        super("O atributo '" + attribute + "' n�o foi preenchido.");
    }
}