/**
 * Classe principal que implementa a lógica de negócios do sistema Jackut.
 * Implementa o padrão Singleton para garantir uma única instância.
 *
 * @author MarcosMelo
 * @version 1.1 (com Singleton Pattern)
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Utils.GlobalFormatter;
import br.ufal.ic.p2.jackut.exceptions.Community.*;
import br.ufal.ic.p2.jackut.exceptions.Relationship.*;
import br.ufal.ic.p2.jackut.exceptions.Message.*;
import br.ufal.ic.p2.jackut.exceptions.Note.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.persistence.*;

import java.util.*;

public class Sistema {
    // Instância única do Singleton
    private static Sistema instance;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final CommunityDAO communityDAO;
    private List<User> users;
    private List<String> sessions;
    private Map<String, Community> communities;

    /**
     * Construtor privado para prevenir instanciação externa.
     */
    private Sistema() {
        this.userDAO = new UserDAO();
        this.sessionDAO = new SessionDAO();
        this.communityDAO = new CommunityDAO();
        this.users = userDAO.load();
        this.sessions = sessionDAO.load();
        this.communities = communityDAO.load();

        if (users == null) users = new ArrayList<>();
        if (sessions == null) sessions = new ArrayList<>();
        if (communities == null) communities = new HashMap<>();
    }

    /**
     * Método estático para obter a instância única do Sistema.
     *
     * @return A instância única do Sistema
     */
    public static synchronized Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    /**
     * Método para reiniciar a instância única (útil para testes).
     */
    public static synchronized void resetInstance() {
        instance = new Sistema();
    }

    /**
     * Reinicia o sistema removendo todos os usuários e sessões.
     */
    public void zerarSistema() {
        this.users = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.communities = new HashMap<>();
        saveData();
    }

    /**
     * Busca um usuário pelo login.
     *
     * @param login Login do usuário a ser encontrado
     * @return Objeto User correspondente
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public User findUserByLogin(String login) throws UserNotFoundException {
        for (User user : users) {
            if (user.getName().equals(login)) {
                return user;
            }
        }

        throw new UserNotFoundException();
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome Nome completo do usuário
     * @param senha Senha de acesso
     * @param login Identificador único
     * @throws UserCreationException Se os dados forem inválidos ou o login já existir
     */
    public void criarUsuario(String nome, String senha, String login) throws UserCreationException {
        if (login == null || nome == null) {
            throw new UserCreationException("Login inválido.");
        } else if (senha == null) {
            throw new UserCreationException("Senha inválida.");
        }

        for (User user : users) {
            if (user.getName().equals(login) || user.getName().equals(nome)) {
                throw new UserCreationException("Conta com esse nome já existe.");
            }
        }

        users.add(new User(nome, senha, login));
        saveData();
    }

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return Login da sessão criada
     * @throws SessionOpeningException Se a autenticação falhar
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException {
        if (login == null || senha == null) {
            throw new SessionOpeningException();
        }

        for (String session : sessions) {
            if (session.equals(login)) {
                return login;
            }
        }

        for (User user : users) {
            if (user != null && user.getName().equals(login)) {
                if (user.getPassword().equals(senha)) {
                    sessions.add(user.getLogin());
                    saveData();
                    return login;
                } else {
                    throw new SessionOpeningException();
                }
            }
        }
        throw new SessionOpeningException();
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
    public void editarPerfil(String id, String atributo, String valor) throws InvalidAttributeException, UserNotFoundException {
        User user = findUserByLogin(id);
        UserProfile profile = user.getProfile();

        if (profile == null) {
            profile = new UserProfile();
            user.setProfile(profile);
        }

        if (!profile.setAtributo(atributo, valor)) {
            throw new InvalidAttributeException();
        }
        saveData();
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

        User user = findUserByLogin(login);
        UserProfile profile = user.getProfile();

        // Atributos que não dependem do perfil
        switch (atributo) {
            case "nome": return user.getLogin();
            case "login": return user.getName();
        }

        if (profile == null || !profile.isAtributoPreenchido(atributo)) {
            throw new AttributeNotSetException();
        }

        String valor = profile.getAtributo(atributo);
        if (valor == null) {
            throw new InvalidAttributeException();
        }
        return valor;
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
            throws UserNotFoundException, FriendshipException {
        User user = findUserByLogin(loginUsuario);
        User amigo = findUserByLogin(loginAmigo);

        this.verficarInimizade(user, amigo);

        if (loginUsuario.equals(loginAmigo)) throw FriendshipException.selfFriendship();
        if (user.getProfile().getAmigosPendentes().contains(loginAmigo))
            throw FriendshipException.pendingFriendRequest();
        if (user.getProfile().getAmigos().contains(loginAmigo))
            throw FriendshipException.alreadyFriends();

        if (amigo.getProfile().getAmigosPendentes().contains(loginUsuario)) {
            user.getProfile().getAmigos().add(loginAmigo);
            amigo.getProfile().getAmigos().add(loginUsuario);
            amigo.getProfile().getAmigosPendentes().remove(loginUsuario);
            user.getProfile().getAmigosPendentes().remove(loginAmigo);
        } else {
            user.getProfile().getAmigosPendentes().add(loginAmigo);
        }
        saveData();
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
        User user = findUserByLogin(loginUsuario);
        User amigo = findUserByLogin(loginAmigo);

        if (!user.getProfile().getAmigos().contains(loginAmigo)) {
            throw FriendshipException.notFriends();
        }

        user.getProfile().getAmigos().remove(loginAmigo);
        amigo.getProfile().getAmigos().remove(loginUsuario);
        saveData();
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
        User user = findUserByLogin(loginUsuario);
        return user.getProfile().getAmigos().contains(loginAmigo);
    }

    /**
     * Obtém a lista de amigos de um usuário formatada.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        User user = findUserByLogin(login);
        return GlobalFormatter.formatList(user.getProfile().getAmigos());
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
        if (loginUsuario.equals(loginRecado)) {
            throw new SelfNoteException();
        }


        User destinatario = findUserByLogin(loginRecado);
        User remetente = findUserByLogin(loginUsuario);

        this.verficarInimizade(remetente, destinatario);

        Note note = new Note(remetente, destinatario, recado);

        destinatario.getProfile().getRecados().offer(note);
        saveData();
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
        User user = findUserByLogin(loginUsuario);
        if (user.getProfile().getRecados().isEmpty()) {
            throw new EmptyNotesException();
        }
        Note recado = user.getProfile().getRecados().poll();
        saveData();
        return recado;
    }

    public void criarComunidade(String loginUsuario, String nome, String descricao) throws CommunityCreationException, UserNotFoundException {
        User dono = findUserByLogin(loginUsuario);
        if(this.communities.containsKey(nome)){
            throw new CommunityCreationException();
        }

        Community comunidade = new Community(nome, descricao, dono);
        this.communities.put(nome, comunidade);

        dono.getProfile().setDonoComunidades(comunidade);
        dono.getProfile().setParticipanteComunidade(comunidade);
        comunidade.addMember(dono);

        saveData();

    }

    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        return communities.get(nome).getDescription();
    }

    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        return communities.get(nome).getOwner().getName();
    }

    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        return communities.get(nome).getMembers();
    }

    public void adicionarComunidade(String loginUsuario, String nome) throws CommunityNotFoundException, UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        user.getProfile().setParticipanteComunidade(this.communities.get(nome));
        this.communities.get(nome).addMember(user);

        saveData();
    }

    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return GlobalFormatter.formatList(user.getProfile().getComunidadesParticipante());
    }

    public void enviarMensagem(String loginUsuario, String nome, String mensagem) throws UserNotFoundException, CommunityNotFoundException {
        User user = findUserByLogin(loginUsuario);
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        Message message = new Message(user, communities.get(nome), mensagem);

        this.communities.get(nome).sendMessage(message);

        saveData();
    }

    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        User user = findUserByLogin(loginUsuario);
        if(user.getProfile().getMensagens().isEmpty()){
            throw new EmptyMessagesException();
        }
        Message mensagem = user.getProfile().getMensagens().poll();
        saveData();
        return mensagem;
    }

    /**
     * Verifica se um usuário tem paquera por outro.
     *
     * @param sessaoId Login da sessão do usuário
     * @param paqueraLogin Login da possível paquera
     * @return true se for paquera, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User paquera = findUserByLogin(paqueraLogin);
        return usuario.getProfile().getPaqueras().contains(paquera);
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
        User idolo = findUserByLogin(idoloLogin);
        User fa = findUserByLogin(loginFa);
        return idolo.getProfile().getFas().contains(fa);
    }


    /**
     * Verifica se um usuário é inimigo de outro.
     *
     * @param sessaoId Login da sessão do usuário
     * @param inimigoLogin Login do possível inimigo
     * @return true se for inimigo, false caso contrário
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);
        return usuario.getProfile().getInimigos().contains(inimigo);
    }

    /**
     * Adiciona uma paquera para o usuário.
     *
     * @param sessaoId Login da sessão do usuário
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */

    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelatioshipException, UserAlreadyAddedException, SelfNoteException {

        User usuario = findUserByLogin(sessaoId);
        User paquera = findUserByLogin(paqueraLogin);

        this.verficarInimizade(usuario, paquera);

        if (sessaoId.equals(paqueraLogin)) {
            throw new SelfRelatioshipException("paquera");
        }

        if (usuario.getProfile().getPaqueras().contains(paquera)) {
            throw new UserAlreadyAddedException("paquera");
        }

        usuario.getProfile().getPaqueras().add(paquera);

        if (paquera.getProfile().getPaqueras().contains(usuario)) {
            this.enviarRecado(paquera.getName(), usuario.getName(), paquera.getLogin() + " é seu paquera - Recado do Jackut.");
            this.enviarRecado(usuario.getName(), paquera.getName(), usuario.getLogin() + " é seu paquera - Recado do Jackut.");
        }
        saveData();
    }


    /**
     * Adiciona um ídolo para o usuário (relação fã-ídolo).
     *
     * @param sessaoId Login da sessão do fã
     * @param idoloLogin Login do ídolo
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelatioshipException, UserAlreadyAddedException {


        User fa = findUserByLogin(sessaoId);
        User idolo = findUserByLogin(idoloLogin);

        this.verficarInimizade(fa, idolo);

        if (sessaoId.equals(idoloLogin)) {
            throw new SelfRelatioshipException("fã");
        }

        if (fa.getProfile().getIdolos().contains(idolo)) {
            throw new UserAlreadyAddedException("ídolo");
        }

        fa.getProfile().getIdolos().add(idolo);
        idolo.getProfile().getFas().add(fa);
        saveData();
    }

    public void adicionarInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException, SelfRelatioshipException, UserAlreadyAddedException {
        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);

        if (sessaoId.equals(inimigoLogin)) {
            throw new SelfRelatioshipException("inimigo");
        }

        if (usuario.getProfile().getInimigos().contains(inimigo)) {
            throw new UserAlreadyAddedException("inimigo");
        }

        usuario.getProfile().getInimigos().add(inimigo);
        inimigo.getProfile().getInimigos().add(usuario);
        saveData();

    }

    public void verficarInimizade(User usuario, User inimigo) throws UserNotFoundException, EnemyAlertException {
        if(usuario.getProfile().getInimigos().contains(inimigo)) {
            throw new EnemyAlertException(inimigo.getLogin());
        }
    }

    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        return GlobalFormatter.formatList(usuario.getProfile().getPaqueras());
    }

    /**
     * Obtém todos os fãs de um usuário.
     *
     * @param loginIdolo Login do ídolo
     * @return Conjunto de logins dos fãs
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        User idolo = findUserByLogin(loginIdolo);
        return GlobalFormatter.formatList(idolo.getProfile().getFas());
    }

    /**
     * Encerra o sistema, persisitindo os dados e limpando a sessão.
     * Também limpa a instância Singleton.
     */
    public void encerrarSistema() {
        try {
            saveData();
            sessions.clear();
            System.out.println("Sistema Jackut encerrado com sucesso. Dados persistidos.");
        } catch (Exception e) {
            System.err.println("Erro ao encerrar o sistema: " + e.getMessage());
            e.printStackTrace();
        } finally {
            instance = null; // Limpa a instância Singleton
        }
    }

    /**
     * Salva os dados atuais no sistema de persistência.
     */
    private void saveData() {
        userDAO.save(users);
        sessionDAO.save(sessions);
        communityDAO.save(communities);
    }
}