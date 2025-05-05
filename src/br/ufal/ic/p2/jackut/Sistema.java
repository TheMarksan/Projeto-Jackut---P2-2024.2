package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.Community.CommunityNotFoundException;
import br.ufal.ic.p2.jackut.exceptions.Message.EmptyMessagesException;
import br.ufal.ic.p2.jackut.models.Message;
import br.ufal.ic.p2.jackut.models.Note;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.persistence.*;
import br.ufal.ic.p2.jackut.services.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.exceptions.Community.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.Note.*;
import br.ufal.ic.p2.jackut.exceptions.Relationship.*;

/**
 * Classe principal que coordena os serviços do sistema Jackut.
 * Implementa o padrão Singleton para garantir uma única instância.
 *
 * <p>Esta classe atua como um facade, delegando as operações para os serviços especializados:</p>
 * <ul>
 *   <li>{@link UserService} - Gerencia usuários, perfis e relacionamentos</li>
 *   <li>{@link SessionService} - Controla autenticação e sessões</li>
 *   <li>{@link CommunityService} - Administra comunidades e mensagens coletivas</li>
 * </ul>
 *
 * @author Marcos Melo
 * @version 2.0 (refatorado com padrão Singleton e serviços especializados)
 */
public class Sistema {
    private static Sistema instance;

    private final UserService userService;
    private final SessionService sessionService;
    private final CommunityService communityService;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final CommunityDAO communityDAO;

    /**
     * Construtor privado para prevenir instanciação externa.
     * Inicializa os DAOs e serviços necessários para o funcionamento do sistema.
     */
    private Sistema() {
        // Inicializa os DAOs
        this.userDAO = new UserDAO();
        this.sessionDAO = new SessionDAO();
        this.communityDAO = new CommunityDAO();

        // Inicializa os serviços com suas dependências
        this.userService = new UserService(userDAO);
        this.sessionService = new SessionService(sessionDAO, userService);
        this.communityService = new CommunityService(communityDAO, userService);
    }

    /**
     * Obtém a instância única do Sistema (Singleton).
     *
     * @return A instância única do sistema Jackut
     */
    public static synchronized Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    /**
     * Reinicia a instância única do sistema (útil para testes).
     */
    public static synchronized void resetInstance() {
        instance = new Sistema();
    }

    // ========== MÉTODOS DE USUÁRIO ==========

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
    public void criarUsuario(String nome, String senha, String login)
            throws InvalidLoginException, InvalidPasswordException, AccountAlreadyExistsException {
        userService.criarUsuario(nome, senha, login);
    }

    /**
     * Busca um usuário pelo seu login.
     *
     * @param login Login do usuário a ser encontrado
     * @return Objeto User correspondente
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public User findUserByLogin(String login) throws UserNotFoundException {
        return userService.findUserByLogin(login);
    }

    /**
     * Edita um atributo do perfil do usuário.
     *
     * @param id Login do usuário
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo não existir
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws InvalidAttributeException, UserNotFoundException {
        userService.editarPerfil(id, atributo, valor);
    }

    /**
     * Obtém o valor de um atributo do usuário.
     *
     * @param login Login do usuário
     * @param atributo Atributo a ser consultado
     * @return Valor do atributo
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws AttributeNotSetException Se o atributo não estiver definido
     * @throws InvalidAttributeException Se o atributo não existir
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UserNotFoundException, AttributeNotSetException, InvalidAttributeException {
        return userService.getAtributoUsuario(login, atributo);
    }

    // ========== MÉTODOS DE SESSÃO ==========

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return Login da sessão criada
     * @throws SessionOpeningException Se a autenticação falhar
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        return sessionService.abrirSessao(login, senha);
    }

    /**
     * Verifica se uma sessão é válida.
     *
     * @param sessionId ID da sessão a ser verificada
     * @return true se a sessão for válida, false caso contrário
     */
    public boolean isSessionValid(String sessionId) {
        return sessionService.isSessionValid(sessionId);
    }

    // ========== MÉTODOS DE RELACIONAMENTOS ==========

    /**
     * Adiciona um amigo para o usuário.
     *
     * @param loginUsuario Login do usuário que está adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se houver pendências ou erros na amizade
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se já forem amigos
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Remove um amigo da lista de amigos.
     *
     * @param loginUsuario Login do usuário
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se não forem amigos
     */
    public void removerAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        userService.removerAmigo(loginUsuario, loginAmigo);
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
        return userService.ehAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Obtém a lista de amigos de um usuário formatada.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        return userService.getAmigos(login);
    }

    // ========== MÉTODOS DE PAQUERAS ==========

    /**
     * Adiciona uma paquera para o usuário.
     *
     * @param sessaoId Login da sessão do usuário
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se já for paquera
     * @throws SelfNoteException Em caso de erro no envio de notificação
     */
    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {
        userService.adicionarPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Obtém a lista de paqueras de um usuário formatada.
     *
     * @param sessaoId Login da sessão do usuário
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        return userService.getPaqueras(sessaoId);
    }

    // ========== MÉTODOS DE FÃS/ÍDOLOS ==========

    /**
     * Adiciona um ídolo para o usuário (torna-se fã).
     *
     * @param sessaoId Login do fã
     * @param idoloLogin Login do ídolo
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se já for fã deste ídolo
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarIdolo(sessaoId, idoloLogin);
    }

    /**
     * Obtém a lista de fãs de um usuário formatada.
     *
     * @param loginIdolo Login do ídolo
     * @return String formatada com a lista de fãs
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        return userService.getFas(loginIdolo);
    }

    // ========== MÉTODOS DE INIMIGOS ==========

    /**
     * Adiciona um inimigo para o usuário (relação mútua).
     *
     * @param sessaoId Login do usuário
     * @param inimigoLogin Login do inimigo
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se já for inimigo
     */
    public void adicionarInimigo(String sessaoId, String inimigoLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Verifica se um usuário é inimigo de outro.
     *
     * @param sessaoId Login do primeiro usuário
     * @param inimigoLogin Login do possível inimigo
     * @return true se forem inimigos, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        return userService.ehInimigo(sessaoId, inimigoLogin);
    }

    // ========== MÉTODOS DE RECADOS ==========

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
        userService.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * Lê o próximo recado não lido do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Objeto Note contendo o recado
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyNotesException Se não houver recados para ler
     */
    public Note lerRecado(String loginUsuario) throws UserNotFoundException, EmptyNotesException {
        return userService.lerRecado(loginUsuario);
    }

    // ========== MÉTODOS DE COMUNIDADES ==========

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param loginUsuario Login do dono da comunidade
     * @param nome Nome da comunidade
     * @param descricao Descrição da comunidade
     * @throws CommunityCreationException Se já existir comunidade com mesmo nome
     * @throws UserNotFoundException Se o usuário dono não for encontrado
     */
    public void criarComunidade(String loginUsuario, String nome, String descricao)
            throws CommunityCreationException, UserNotFoundException {
        communityService.criarComunidade(loginUsuario, nome, descricao);
    }

    /**
     * Obtém a descrição de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descrição da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getDescricaoComunidade(nome);
    }

    /**
     * Obtém o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getDonoComunidade(nome);
    }

    /**
     * Obtém a lista de membros de uma comunidade formatada.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getMembrosComunidade(nome);
    }

    /**
     * Adiciona um usuário como membro de uma comunidade.
     *
     * @param loginUsuario Login do usuário a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void adicionarComunidade(String loginUsuario, String nome)
            throws CommunityNotFoundException, UserNotFoundException {
        communityService.adicionarMembroComunidade(loginUsuario, nome);
    }

    /**
     * Obtém a lista de comunidades de um usuário formatada.
     *
     * @param loginUsuario Login do usuário
     * @return String formatada com as comunidades
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        return userService.getComunidadesUsuario(loginUsuario);
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     *
     * @param loginUsuario Login do remetente
     * @param nome Nome da comunidade
     * @param mensagem Conteúdo da mensagem
     * @throws UserNotFoundException Se o remetente não for encontrado
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public void enviarMensagem(String loginUsuario, String nome, String mensagem)
            throws UserNotFoundException, CommunityNotFoundException {
        communityService.enviarMensagemComunidade(loginUsuario, nome, mensagem);
    }

    /**
     * Lê a próxima mensagem na fila do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyMessagesException Se não houver mensagens para ler
     */
    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return userService.lerMensagem(loginUsuario);
    }

    /**
     * Verifica se um usuário tem paquera por outro.
     *
     * @param sessaoId Login do usuário
     * @param paqueraLogin Login da possível paquera
     * @return true se for paquera, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        return userService.ehPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Verifica se um usuário é fã de outro.
     *
     * @param loginFa Login do possível fã
     * @param idoloLogin Login do possível ídolo
     * @return true se for fã, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehFa(String loginFa, String idoloLogin) throws UserNotFoundException {
        return userService.ehFa(loginFa, idoloLogin);
    }

    /**
     * Remove completamente um usuário do sistema.
     *
     * @param sessaoId Login do usuário a ser removido
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        sessionService.fecharSessao(sessaoId);
        communityService.deletarComunidadesDono(sessaoId);
        userService.removerUsuario(sessaoId);
    }

    // ========== MÉTODOS DE GERENCIAMENTO DO SISTEMA ==========

    /**
     * Reinicia o sistema, removendo todos os usuários, sessões e comunidades.
     */
    public void zerarSistema() {
        userService.getUsers().clear();
        sessionService.limparSessoes();
        communityService.limparComunidades();
        saveData();
    }

    /**
     * Encerra o sistema, persistindo todos os dados.
     */
    public void encerrarSistema() {
        saveData();
        sessionService.limparSessoes();
    }

    /**
     * Salva os dados atuais no sistema de persistência.
     */
    private void saveData() {
        userDAO.save(userService.getUsers());
        sessionDAO.save(sessionService.getActiveSessions());
        communityDAO.save(communityService.getCommunities());
    }
}