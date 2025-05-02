package br.ufal.ic.p2.jackut.exceptions.Relationship;

/**
 * Exceção lançada quando uma operação é bloqueada devido a uma relação de inimizade entre usuários.
 * <p>
 * Esta é uma {@link RuntimeException} que indica tentativa de interação entre usuários
 * que estão marcados como inimigos no sistema.
 * </p>
 *
 * <p><b>Situações comuns:</b></p>
 * <ul>
 *   <li>Tentativa de envio de mensagem para um inimigo</li>
 *   <li>Solicitação de amizade com usuário inimigo</li>
 *   <li>Operações que requerem confiança entre usuários</li>
 * </ul>
 *
 * <p><b>Fluxo recomendado:</b></p>
 * <ol>
 *   <li>Capturar a exceção no ponto adequado</li>
 *   <li>Informar ao usuário sobre a restrição</li>
 *   <li>Oferecer alternativas quando aplicável</li>
 *   <li>Registrar o evento para análise de segurança</li>
 * </ol>
 */
public class EnemyAlertException extends RuntimeException {

    /**
     * Cria uma exceção com mensagem específica sobre o usuário inimigo.
     *
     * @param enemy Login ou identificação do usuário inimigo (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se o parâmetro enemy for nulo ou vazio
     */
    public EnemyAlertException(String enemy) {
        super("Função inválida: " + enemy + " é seu inimigo.");
    }
}