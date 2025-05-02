package br.ufal.ic.p2.jackut.exceptions.User;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar um usu�rio que n�o existe no sistema.
 * <p>
 * Esta exce��o verificada (checked exception) � uma das mais comuns no sistema,
 * sendo lan�ada sempre que um login ou identificador de usu�rio n�o corresponde
 * a nenhum usu�rio cadastrado.
 * </p>
 *
 * <p><b>Cen�rios t�picos:</b></p>
 * <ul>
 *   <li>Busca por usu�rio com login inexistente</li>
 *   <li>Tentativa de acesso a perfil n�o cadastrado</li>
 *   <li>Opera��es com refer�ncias a usu�rios inv�lidos</li>
 * </ul>
 *
 * <p><b>Recomenda��es de tratamento:</b></p>
 * <ol>
 *   <li>Verificar se o login foi digitado corretamente</li>
 *   <li>Oferecer op��o de cadastro quando aplic�vel</li>
 *   <li>Registrar tentativas de acesso a usu�rios inexistentes</li>
 *   <li>Fornecer feedback claro ao usu�rio final</li>
 * </ol>
 */
public class UserNotFoundException extends Exception {

    /**
     * Cria uma exce��o com mensagem padr�o: "Usu�rio n�o cadastrado."
     */
    public UserNotFoundException() {
        super("Usu�rio n�o cadastrado.");
    }

    /**
     * Cria uma exce��o com mensagem personalizada incluindo o nome de usu�rio.
     *
     * @param username Login do usu�rio n�o encontrado (n�o pode ser nulo)
     * @throws IllegalArgumentException Se o username for nulo
     */
    public UserNotFoundException(String username) {
        super("O usu�rio '" + username + "' n�o est� cadastrado.");
    }
}