package br.ufal.ic.p2.jackut.exceptions.User;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário que não existe no sistema.
 * <p>
 * Esta exceção verificada (checked exception) é uma das mais comuns no sistema,
 * sendo lançada sempre que um login ou identificador de usuário não corresponde
 * a nenhum usuário cadastrado.
 * </p>
 *
 * <p><b>Cenários típicos:</b></p>
 * <ul>
 *   <li>Busca por usuário com login inexistente</li>
 *   <li>Tentativa de acesso a perfil não cadastrado</li>
 *   <li>Operações com referências a usuários inválidos</li>
 * </ul>
 *
 * <p><b>Recomendações de tratamento:</b></p>
 * <ol>
 *   <li>Verificar se o login foi digitado corretamente</li>
 *   <li>Oferecer opção de cadastro quando aplicável</li>
 *   <li>Registrar tentativas de acesso a usuários inexistentes</li>
 *   <li>Fornecer feedback claro ao usuário final</li>
 * </ol>
 */
public class UserNotFoundException extends Exception {

    /**
     * Cria uma exceção com mensagem padrão: "Usuário não cadastrado."
     */
    public UserNotFoundException() {
        super("Usuário não cadastrado.");
    }

    /**
     * Cria uma exceção com mensagem personalizada incluindo o nome de usuário.
     *
     * @param username Login do usuário não encontrado (não pode ser nulo)
     * @throws IllegalArgumentException Se o username for nulo
     */
    public UserNotFoundException(String username) {
        super("O usuário '" + username + "' não está cadastrado.");
    }
}