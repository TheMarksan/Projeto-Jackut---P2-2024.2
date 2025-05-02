package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exce��o lan�ada quando uma opera��o � bloqueada devido a uma rela��o de inimizade entre usu�rios.
 * <p>
 * Esta � uma {@link RuntimeException} que indica tentativa de intera��o entre usu�rios
 * que est�o marcados como inimigos no sistema.
 * </p>
 *
 * <p><b>Situa��es comuns:</b></p>
 * <ul>
 *   <li>Tentativa de envio de mensagem para um inimigo</li>
 *   <li>Solicita��o de amizade com usu�rio inimigo</li>
 *   <li>Opera��es que requerem confian�a entre usu�rios</li>
 * </ul>
 *
 * <p><b>Fluxo recomendado:</b></p>
 * <ol>
 *   <li>Capturar a exce��o no ponto adequado</li>
 *   <li>Informar ao usu�rio sobre a restri��o</li>
 *   <li>Oferecer alternativas quando aplic�vel</li>
 *   <li>Registrar o evento para an�lise de seguran�a</li>
 * </ol>
 */
public class EnemyAlertException extends RuntimeException {

    /**
     * Cria uma exce��o com mensagem espec�fica sobre o usu�rio inimigo.
     *
     * @param enemy Login ou identifica��o do usu�rio inimigo (n�o pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se o par�metro enemy for nulo ou vazio
     */
    public EnemyAlertException(String enemy) {
        super("Fun��o inv�lida: " + enemy + " � seu inimigo.");
    }
}