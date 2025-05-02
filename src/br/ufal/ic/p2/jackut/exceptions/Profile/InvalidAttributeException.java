package br.ufal.ic.p2.jackut.exceptions.Profile;

/**
 * Exceção lançada quando um atributo de perfil possui valor inválido ou não existe.
 * <p>
 * Esta é uma exceção verificada (checked exception) que indica problemas de validação
 * ou consistência em atributos do perfil do usuário.
 * </p>
 *
 * <p><b>Cenários típicos:</b></p>
 * <ul>
 *   <li>Valor de atributo fora do domínio permitido</li>
 *   <li>Tipo de dado incompatível com o atributo</li>
 *   <li>Tentativa de acesso a atributo não existente</li>
 *   <li>Formato inválido para o atributo especificado</li>
 * </ul>
 *
 * <p><b>Sugestões de tratamento:</b></p>
 * <ol>
 *   <li>Validar os valores antes de atribuí-los</li>
 *   <li>Verificar a documentação dos atributos obrigatórios</li>
 *   <li>Fornecer feedback claro ao usuário sobre o erro específico</li>
 *   <li>Implementar tratamento adequado para valores inválidos</li>
 * </ol>
 */
public class InvalidAttributeException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão: "Atributo inválido."
     */
    public InvalidAttributeException() {
        super("Atributo inválido.");
    }

    /**
     * Cria uma exceção com mensagem específica para o atributo inválido.
     *
     * @param attribute Nome do atributo inválido (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException Se o nome do atributo for nulo ou vazio
     */
    public InvalidAttributeException(String attribute) {
        super("O atributo '" + attribute + "' é inválido ou inexistente.");
    }
}