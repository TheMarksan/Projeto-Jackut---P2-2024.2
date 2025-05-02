package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exce��o lan�ada quando ocorre uma falha no envio de um recado.
 * <p>
 * Esta exce��o verificada (checked exception) engloba diversos tipos de problemas
 * que podem ocorrer durante o processo de envio de recados entre usu�rios.
 * </p>
 *
 * <p><b>Poss�veis causas:</b></p>
 * <ul>
 *   <li>Problemas de valida��o do recado</li>
 *   <li>Falhas na persist�ncia dos dados</li>
 *   <li>Erros na entrega para o destinat�rio</li>
 *   <li>Restri��es de seguran�a</li>
 * </ul>
 *
 * <p><b>Recomenda��es de tratamento:</b></p>
 * <ol>
 *   <li>Registrar o erro para an�lise</li>
 *   <li>Informar ao usu�rio sobre a falha</li>
 *   <li>Oferecer op��o de tentar novamente quando aplic�vel</li>
 * </ol>
 */
public class SendNoteException extends Exception {

    /**
     * Cria uma exce��o com mensagem de erro especificada.
     *
     * @param message Descri��o detalhada do erro (n�o pode ser nula ou vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SendNoteException(String message) {
        super(message);
    }

    /**
     * Cria uma exce��o com mensagem e causa espec�ficas.
     * <p>
     * �til para encadeamento de exce��es quando o erro � resultado
     * de outra exce��o.
     * </p>
     *
     * @param message Descri��o detalhada do erro (n�o pode ser nula ou vazia)
     * @param cause Exce��o original que causou o problema (pode ser nula)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SendNoteException(String message, Throwable cause) {
        super(message, cause);
    }
}