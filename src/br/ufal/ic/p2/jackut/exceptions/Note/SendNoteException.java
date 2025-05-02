package br.ufal.ic.p2.jackut.exceptions.Note;

/**
 * Exceção lançada quando ocorre uma falha no envio de um recado.
 * <p>
 * Esta exceção verificada (checked exception) engloba diversos tipos de problemas
 * que podem ocorrer durante o processo de envio de recados entre usuários.
 * </p>
 *
 * <p><b>Possíveis causas:</b></p>
 * <ul>
 *   <li>Problemas de validação do recado</li>
 *   <li>Falhas na persistência dos dados</li>
 *   <li>Erros na entrega para o destinatário</li>
 *   <li>Restrições de segurança</li>
 * </ul>
 *
 * <p><b>Recomendações de tratamento:</b></p>
 * <ol>
 *   <li>Registrar o erro para análise</li>
 *   <li>Informar ao usuário sobre a falha</li>
 *   <li>Oferecer opção de tentar novamente quando aplicável</li>
 * </ol>
 */
public class SendNoteException extends Exception {

    /**
     * Cria uma exceção com mensagem de erro especificada.
     *
     * @param message Descrição detalhada do erro (não pode ser nula ou vazia)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SendNoteException(String message) {
        super(message);
    }

    /**
     * Cria uma exceção com mensagem e causa específicas.
     * <p>
     * Útil para encadeamento de exceções quando o erro é resultado
     * de outra exceção.
     * </p>
     *
     * @param message Descrição detalhada do erro (não pode ser nula ou vazia)
     * @param cause Exceção original que causou o problema (pode ser nula)
     * @throws IllegalArgumentException Se a mensagem for nula ou vazia
     */
    public SendNoteException(String message, Throwable cause) {
        super(message, cause);
    }
}