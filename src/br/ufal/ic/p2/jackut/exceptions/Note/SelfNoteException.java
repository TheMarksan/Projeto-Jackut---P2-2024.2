package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exceção lançada quando um usuário tenta enviar um recado para si mesmo.
 * <p>
 * Esta é uma exceção verificada (checked exception) que indica uma violação
 * das regras de negócio do sistema, onde recados auto-destinados não são permitidos.
 * </p>
 *
 * <p><b>Situações comuns:</b></p>
 * <ul>
 *   <li>Tentativa de envio de recado com remetente igual ao destinatário</li>
 *   <li>Operações de encaminhamento mal configuradas</li>
 *   <li>Erros na seleção do destinatário</li>
 * </ul>
 *
 * <p><b>Como tratar:</b> Normalmente deve-se:</p>
 * <ol>
 *   <li>Validar remetente e destinatário antes do envio</li>
 *   <li>Informar ao usuário sobre o erro</li>
 *   <li>Solicitar novo destinatário quando aplicável</li>
 * </ol>
 */
public class SelfNoteException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão:
     * "Usuário não pode enviar recado para si mesmo."
     */
    public SelfNoteException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }

    /**
     * Cria uma exceção com uma mensagem personalizada.
     *
     * @param message Mensagem detalhada sobre o erro (deve ser não-nula e não-vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SelfNoteException(String message) {
        super(message);
    }
}