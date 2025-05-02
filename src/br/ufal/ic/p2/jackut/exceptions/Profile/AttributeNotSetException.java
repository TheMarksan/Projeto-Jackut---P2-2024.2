package br.ufal.ic.p2.jackut.exceptions.Profile;

/**
 * Exceção lançada quando um atributo obrigatório do perfil não foi preenchido.
 * <p>
 * Esta exceção verificada (checked exception) indica a ausência de valores
 * obrigatórios em campos do perfil do usuário.
 * </p>
 *
 * <p><b>Contextos comuns:</b></p>
 * <ul>
 *   <li>Tentativa de acesso a atributos não inicializados</li>
 *   <li>Validação de perfis incompletos</li>
 *   <li>Operações que requerem atributos específicos</li>
 * </ul>
 *
 * <p><b>Como resolver:</b></p>
 * <ol>
 *   <li>Verificar quais atributos são obrigatórios</li>
 *   <li>Solicitar ao usuário o preenchimento dos campos faltantes</li>
 *   <li>Validar o perfil antes de operações críticas</li>
 * </ol>
 */
public class AttributeNotSetException extends Exception {

    /**
     * Cria uma exceção com a mensagem padrão: "Atributo não preenchido."
     */
    public AttributeNotSetException() {
        super("Atributo não preenchido.");
    }

    /**
     * Cria uma exceção com mensagem específica para o atributo faltante.
     *
     * @param attribute Nome do atributo não preenchido (não pode ser nulo)
     * @throws IllegalArgumentException Se o nome do atributo for nulo
     */
    public AttributeNotSetException(String attribute) {
        super("O atributo '" + attribute + "' não foi preenchido.");
    }
}