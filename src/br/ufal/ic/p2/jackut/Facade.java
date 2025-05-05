package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.Community.CommunityCreationException;
import br.ufal.ic.p2.jackut.exceptions.Community.CommunityNotFoundException;
import br.ufal.ic.p2.jackut.exceptions.Relationship.*;
import br.ufal.ic.p2.jackut.exceptions.Message.EmptyMessagesException;
import br.ufal.ic.p2.jackut.exceptions.Note.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.models.*;

/**
 * Fachada principal do sistema Jackut que fornece a interface pública para interação com o sistema.
 * Atua como um ponto único de acesso para todas as operações do sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */
public class Facade {
    private final Sistema sistema;

    /**
     * Construtor que inicializa a fachada criando uma nova instância do sistema.
     */
    public Facade() {
        this.sistema = Sistema.getInstance();
    }

    /**
     * Reinicia o sistema removendo todos os usuários e sessões.
     */
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    /**
     * Obtém o valor de um atributo específico do usuário.
     *
     * @param login Login do usuário
     * @param atributo Atributo a ser consultado
     * @return Valor do atributo solicitado
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws AttributeNotSetException Se o atributo não estiver definido
     * @throws InvalidAttributeException Se o atributo não existir
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UserNotFoundException, AttributeNotSetException, InvalidAttributeException {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome Nome completo do usuário
     * @param senha Senha de acesso
     * @param login Identificador único
     * @throws InvalidLoginException Se o login for inválido
     * @throws InvalidPasswordException Se a senha for inválida
     * @throws AccountAlreadyExistsException Se o login já existir
     */
    public void criarUsuario(String nome, String senha, String login) throws InvalidLoginException, InvalidPasswordException, AccountAlreadyExistsException {
        sistema.criarUsuario(nome, senha, login);
    }

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return Login da sessão criada
     * @throws SessionOpeningException Se a autenticação falhar
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Encerra o sistema, salvando os dados atuais.
     */
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    /**
     * Edita um atributo do perfil do usuário.
     *
     * @param id Login do usuário
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo não existir
     * @throws ProfileEditException Se ocorrer erro na edição do perfil
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws InvalidAttributeException, ProfileEditException, UserNotFoundException {
        try {
            sistema.editarPerfil(id, atributo, valor);
        } catch (Exception e) {
            throw new ProfileEditException(e.getMessage());
        }
    }

    /**
     * Adiciona um amigo para o usuário.
     *
     * @param loginUsuario Login do usuário que está adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se já forem amigos ou houver solicitação pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException, SelfRelationshipException, UserAlreadyAddedException {
        sistema.adicionarAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Remove um amigo da lista de amigos do usuário.
     *
     * @param loginUsuario Login do usuário
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se não forem amigos
     */
    public void removerAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        sistema.removerAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param loginUsuario Login do primeiro usuário
     * @param loginAmigo Login do segundo usuário
     * @return true se forem amigos, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserNotFoundException {
        return sistema.ehAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Obtém a lista de amigos de um usuário formatada.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinatário
     * @param recado Conteúdo do recado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfNoteException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfNoteException {
        sistema.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * Lê o próximo recado não lido do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Conteúdo do recado
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyNotesException Se não houver mensagens para ler
     */
    public Note lerRecado(String loginUsuario) throws UserNotFoundException, EmptyNotesException {
        return sistema.lerRecado(loginUsuario);
    }

    /**
     * Cria uma nova comunidade com o nome e descrição fornecidos, pertencente ao usuário especificado.
     *
     * @param loginUsuario Login do usuário que está criando a comunidade
     * @param nome Nome da comunidade a ser criada
     * @param descricao Descrição da comunidade
     * @throws CommunityCreationException Se ocorrer um erro ao criar a comunidade
     * @throws UserNotFoundException Se o usuário especificado não existir
     */
    public void criarComunidade(String loginUsuario, String nome, String descricao) throws CommunityCreationException, UserNotFoundException {
        sistema.criarComunidade(loginUsuario, nome, descricao);
    }

    /**
     * Obtém a descrição de uma comunidade existente.
     *
     * @param nome Nome da comunidade
     * @return Descrição da comunidade
     * @throws CommunityNotFoundException Se a comunidade não for encontrada
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getDescricaoComunidade(nome);
    }

    /**
     * Obtém o dono (criador) de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade não for encontrada
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getDonoComunidade(nome);
    }

    /**
     * Obtém a lista de membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Lista de membros da comunidade
     * @throws CommunityNotFoundException Se a comunidade não for encontrada
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getMembrosComunidade(nome);
    }

    /**
     * Adiciona um usuário a uma comunidade existente.
     *
     * @param loginUsuario Login do usuário a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade não for encontrada
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void adicionarComunidade(String loginUsuario, String nome) throws CommunityNotFoundException, UserNotFoundException {
        sistema.adicionarComunidade(loginUsuario, nome);
    }

    /**
     * Obtém a lista de comunidades às quais um usuário pertence.
     *
     * @param loginUsuario Login do usuário
     * @return Lista de comunidades do usuário
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        return sistema.getComunidades(loginUsuario);
    }

    /**
     * Envia uma mensagem para uma comunidade.
     *
     * @param loginUsuario Login do usuário que está enviando a mensagem
     * @param nome Nome da comunidade destinatária
     * @param mensagem Conteúdo da mensagem
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws CommunityNotFoundException Se a comunidade não for encontrada
     */
    public void enviarMensagem(String loginUsuario, String nome, String mensagem) throws UserNotFoundException, CommunityNotFoundException {
        sistema.enviarMensagem(loginUsuario, nome, mensagem);
    }

    /**
     * Lê a próxima mensagem destinada ao usuário.
     *
     * @param loginUsuario Login do usuário que está lendo a mensagem
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyMessagesException Se não houver mensagens para ler
     */
    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return sistema.lerMensagem(loginUsuario);
    }

    /**
     * Verifica se um usuário é inimigo de outro.
     *
     * @param sessaoId Login do usuário que está verificando
     * @param inimigoLogin Login do possível inimigo
     * @return true se for inimigo, false caso contrário
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        return sistema.ehInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Verifica se um usuário tem outro como paquera.
     *
     * @param sessaoId Login do usuário que está verificando
     * @param paqueraLogin Login da possível paquera
     * @return true se for paquera, false caso contrário
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        return sistema.ehPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Verifica se um usuário é fã de outro.
     *
     * @param loginFa Login do possível fã
     * @param idoloLogin Login do possível ídolo
     * @return true se for fã, false caso contrário
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     */
    public boolean ehFa(String loginFa, String idoloLogin) throws UserNotFoundException {
        return sistema.ehFa(loginFa, idoloLogin);
    }

    /**
     * Adiciona um usuário como paquera de outro.
     *
     * @param sessaoId Login do usuário que está adicionando a paquera
     * @param paqueraLogin Login da paquera a ser adicionada
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se a paquera já foi adicionada anteriormente
     * @throws SelfNoteException Se tentar adicionar uma relação consigo mesmo
     */
    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {
        sistema.adicionarPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Adiciona um usuário como ídolo de outro.
     *
     * @param sessaoId Login do usuário que está adicionando o ídolo
     * @param idoloLogin Login do ídolo a ser adicionado
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     * @throws UserAlreadyAddedException Se o ídolo já foi adicionado anteriormente
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin) throws UserNotFoundException, UserAlreadyAddedException, SelfRelationshipException {
        sistema.adicionarIdolo(sessaoId, idoloLogin);
    }

    /**
     * Adiciona um usuário como inimigo de outro.
     *
     * @param sessaoId Login do usuário que está adicionando o inimigo
     * @param inimigoLogin Login do inimigo a ser adicionado
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o inimigo já foi adicionado anteriormente
     */
    public void adicionarInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        sistema.adicionarInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Obtém a lista de paqueras de um usuário.
     *
     * @param sessaoId Login do usuário
     * @return Lista de paqueras do usuário
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        return sistema.getPaqueras(sessaoId);
    }

    /**
     * Obtém a lista de fãs de um usuário.
     *
     * @param loginIdolo Login do ídolo
     * @return Lista de fãs do usuário
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        return sistema.getFas(loginIdolo);
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param sessaoId Login do usuário a ser removido
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        sistema.removerUsuario(sessaoId);
    }

}
