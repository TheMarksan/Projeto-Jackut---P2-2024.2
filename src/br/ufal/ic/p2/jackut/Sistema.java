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
 * Classe principal que coordena os servi�os do sistema Jackut.
 * Implementa o padr�o Singleton para garantir uma �nica inst�ncia.
 *
 * <p>Esta classe atua como um facade, delegando as opera��es para os servi�os especializados:</p>
 * <ul>
 *   <li>{@link UserService} - Gerencia usu�rios, perfis e relacionamentos</li>
 *   <li>{@link SessionService} - Controla autentica��o e sess�es</li>
 *   <li>{@link CommunityService} - Administra comunidades e mensagens coletivas</li>
 * </ul>
 *
 * @author Marcos Melo
 * @version 2.0 (refatorado com padr�o Singleton e servi�os especializados)
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
     * Construtor privado para prevenir instancia��o externa.
     * Inicializa os DAOs e servi�os necess�rios para o funcionamento do sistema.
     */
    private Sistema() {
        // Inicializa os DAOs
        this.userDAO = new UserDAO();
        this.sessionDAO = new SessionDAO();
        this.communityDAO = new CommunityDAO();

        // Inicializa os servi�os com suas depend�ncias
        this.userService = new UserService(userDAO);
        this.sessionService = new SessionService(sessionDAO, userService);
        this.communityService = new CommunityService(communityDAO, userService);
    }

    /**
     * Obt�m a inst�ncia �nica do Sistema (Singleton).
     *
     * @return A inst�ncia �nica do sistema Jackut
     */
    public static synchronized Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    /**
     * Reinicia a inst�ncia �nica do sistema (�til para testes).
     */
    public static synchronized void resetInstance() {
        instance = new Sistema();
    }

    // ========== M�TODOS DE USU�RIO ==========

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param nome Nome completo do usu�rio
     * @param senha Senha de acesso
     * @param login Identificador �nico
     * @throws InvalidLoginException Se o login for inv�lido
     * @throws InvalidPasswordException Se a senha for inv�lida
     * @throws AccountAlreadyExistsException Se o login j� existir
     */
    public void criarUsuario(String nome, String senha, String login)
            throws InvalidLoginException, InvalidPasswordException, AccountAlreadyExistsException {
        userService.criarUsuario(nome, senha, login);
    }

    /**
     * Busca um usu�rio pelo seu login.
     *
     * @param login Login do usu�rio a ser encontrado
     * @return Objeto User correspondente
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public User findUserByLogin(String login) throws UserNotFoundException {
        return userService.findUserByLogin(login);
    }

    /**
     * Edita um atributo do perfil do usu�rio.
     *
     * @param id Login do usu�rio
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo n�o existir
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws InvalidAttributeException, UserNotFoundException {
        userService.editarPerfil(id, atributo, valor);
    }

    /**
     * Obt�m o valor de um atributo do usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Atributo a ser consultado
     * @return Valor do atributo
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws AttributeNotSetException Se o atributo n�o estiver definido
     * @throws InvalidAttributeException Se o atributo n�o existir
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UserNotFoundException, AttributeNotSetException, InvalidAttributeException {
        return userService.getAtributoUsuario(login, atributo);
    }

    // ========== M�TODOS DE SESS�O ==========

    /**
     * Autentica um usu�rio e inicia uma nova sess�o.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return Login da sess�o criada
     * @throws SessionOpeningException Se a autentica��o falhar
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        return sessionService.abrirSessao(login, senha);
    }

    /**
     * Verifica se uma sess�o � v�lida.
     *
     * @param sessionId ID da sess�o a ser verificada
     * @return true se a sess�o for v�lida, false caso contr�rio
     */
    public boolean isSessionValid(String sessionId) {
        return sessionService.isSessionValid(sessionId);
    }

    // ========== M�TODOS DE RELACIONAMENTOS ==========

    /**
     * Adiciona um amigo para o usu�rio.
     *
     * @param loginUsuario Login do usu�rio que est� adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se houver pend�ncias ou erros na amizade
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se j� forem amigos
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Remove um amigo da lista de amigos.
     *
     * @param loginUsuario Login do usu�rio
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se n�o forem amigos
     */
    public void removerAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        userService.removerAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param loginUsuario Login do primeiro usu�rio
     * @param loginAmigo Login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserNotFoundException {
        return userService.ehAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio formatada.
     *
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        return userService.getAmigos(login);
    }

    // ========== M�TODOS DE PAQUERAS ==========

    /**
     * Adiciona uma paquera para o usu�rio.
     *
     * @param sessaoId Login da sess�o do usu�rio
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se j� for paquera
     * @throws SelfNoteException Em caso de erro no envio de notifica��o
     */
    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {
        userService.adicionarPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Obt�m a lista de paqueras de um usu�rio formatada.
     *
     * @param sessaoId Login da sess�o do usu�rio
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        return userService.getPaqueras(sessaoId);
    }

    // ========== M�TODOS DE F�S/�DOLOS ==========

    /**
     * Adiciona um �dolo para o usu�rio (torna-se f�).
     *
     * @param sessaoId Login do f�
     * @param idoloLogin Login do �dolo
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se j� for f� deste �dolo
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarIdolo(sessaoId, idoloLogin);
    }

    /**
     * Obt�m a lista de f�s de um usu�rio formatada.
     *
     * @param loginIdolo Login do �dolo
     * @return String formatada com a lista de f�s
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        return userService.getFas(loginIdolo);
    }

    // ========== M�TODOS DE INIMIGOS ==========

    /**
     * Adiciona um inimigo para o usu�rio (rela��o m�tua).
     *
     * @param sessaoId Login do usu�rio
     * @param inimigoLogin Login do inimigo
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se j� for inimigo
     */
    public void adicionarInimigo(String sessaoId, String inimigoLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        userService.adicionarInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Verifica se um usu�rio � inimigo de outro.
     *
     * @param sessaoId Login do primeiro usu�rio
     * @param inimigoLogin Login do poss�vel inimigo
     * @return true se forem inimigos, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        return userService.ehInimigo(sessaoId, inimigoLogin);
    }

    // ========== M�TODOS DE RECADOS ==========

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinat�rio
     * @param recado Conte�do do recado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfNoteException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfNoteException {
        userService.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Objeto Note contendo o recado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyNotesException Se n�o houver recados para ler
     */
    public Note lerRecado(String loginUsuario) throws UserNotFoundException, EmptyNotesException {
        return userService.lerRecado(loginUsuario);
    }

    // ========== M�TODOS DE COMUNIDADES ==========

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param loginUsuario Login do dono da comunidade
     * @param nome Nome da comunidade
     * @param descricao Descri��o da comunidade
     * @throws CommunityCreationException Se j� existir comunidade com mesmo nome
     * @throws UserNotFoundException Se o usu�rio dono n�o for encontrado
     */
    public void criarComunidade(String loginUsuario, String nome, String descricao)
            throws CommunityCreationException, UserNotFoundException {
        communityService.criarComunidade(loginUsuario, nome, descricao);
    }

    /**
     * Obt�m a descri��o de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descri��o da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getDescricaoComunidade(nome);
    }

    /**
     * Obt�m o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getDonoComunidade(nome);
    }

    /**
     * Obt�m a lista de membros de uma comunidade formatada.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        return communityService.getMembrosComunidade(nome);
    }

    /**
     * Adiciona um usu�rio como membro de uma comunidade.
     *
     * @param loginUsuario Login do usu�rio a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void adicionarComunidade(String loginUsuario, String nome)
            throws CommunityNotFoundException, UserNotFoundException {
        communityService.adicionarMembroComunidade(loginUsuario, nome);
    }

    /**
     * Obt�m a lista de comunidades de um usu�rio formatada.
     *
     * @param loginUsuario Login do usu�rio
     * @return String formatada com as comunidades
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        return userService.getComunidadesUsuario(loginUsuario);
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     *
     * @param loginUsuario Login do remetente
     * @param nome Nome da comunidade
     * @param mensagem Conte�do da mensagem
     * @throws UserNotFoundException Se o remetente n�o for encontrado
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public void enviarMensagem(String loginUsuario, String nome, String mensagem)
            throws UserNotFoundException, CommunityNotFoundException {
        communityService.enviarMensagemComunidade(loginUsuario, nome, mensagem);
    }

    /**
     * L� a pr�xima mensagem na fila do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens para ler
     */
    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return userService.lerMensagem(loginUsuario);
    }

    /**
     * Verifica se um usu�rio tem paquera por outro.
     *
     * @param sessaoId Login do usu�rio
     * @param paqueraLogin Login da poss�vel paquera
     * @return true se for paquera, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        return userService.ehPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Verifica se um usu�rio � f� de outro.
     *
     * @param loginFa Login do poss�vel f�
     * @param idoloLogin Login do poss�vel �dolo
     * @return true se for f�, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehFa(String loginFa, String idoloLogin) throws UserNotFoundException {
        return userService.ehFa(loginFa, idoloLogin);
    }

    /**
     * Remove completamente um usu�rio do sistema.
     *
     * @param sessaoId Login do usu�rio a ser removido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        sessionService.fecharSessao(sessaoId);
        communityService.deletarComunidadesDono(sessaoId);
        userService.removerUsuario(sessaoId);
    }

    // ========== M�TODOS DE GERENCIAMENTO DO SISTEMA ==========

    /**
     * Reinicia o sistema, removendo todos os usu�rios, sess�es e comunidades.
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
     * Salva os dados atuais no sistema de persist�ncia.
     */
    private void saveData() {
        userDAO.save(userService.getUsers());
        sessionDAO.save(sessionService.getActiveSessions());
        communityDAO.save(communityService.getCommunities());
    }
}