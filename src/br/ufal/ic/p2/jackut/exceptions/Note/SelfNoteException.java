package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exce��o lan�ada quando um usu�rio tenta enviar um recado para si mesmo.
 * <p>
 * Esta � uma exce��o verificada (checked exception) que indica uma viola��o
 * das regras de neg�cio do sistema, onde recados auto-destinados n�o s�o permitidos.
 * </p>
 *
 * <p><b>Situa��es comuns:</b></p>
 * <ul>
 *   <li>Tentativa de envio de recado com remetente igual ao destinat�rio</li>
 *   <li>Opera��es de encaminhamento mal configuradas</li>
 *   <li>Erros na sele��o do destinat�rio</li>
 * </ul>
 *
 * <p><b>Como tratar:</b> Normalmente deve-se:</p>
 * <ol>
 *   <li>Validar remetente e destinat�rio antes do envio</li>
 *   <li>Informar ao usu�rio sobre o erro</li>
 *   <li>Solicitar novo destinat�rio quando aplic�vel</li>
 * </ol>
 */
public class SelfNoteException extends Exception {

    /**
     * Cria uma exce��o com a mensagem padr�o:
     * "Usu�rio n�o pode enviar recado para si mesmo."
     */
    public SelfNoteException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }

    /**
     * Cria uma exce��o com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (deve ser n�o-nula e n�o-vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SelfNoteException(String message) {
        super(message);
    }
}