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

    /**
     * Cria uma nova comunidade no sistema com o nome e descrição especificados,
     * associando o usuário informado como dono e primeiro membro.
     *
     * @param loginUsuario Login do usuário que será o dono da comunidade
     * @param nome Nome da comunidade a ser criada (deve ser único)
     * @param descricao Descrição da comunidade
     * @throws CommunityCreationException Se já existir uma comunidade com o mesmo nome
     * @throws UserNotFoundException Se o usuário dono não for encontrado
     */
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

    /**
     * Recupera a descrição de uma comunidade existente.
     *
     * @param nome Nome da comunidade buscada
     * @return String com a descrição da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getDescription();
    }

    /**
     * Obtém o nome do dono de uma comunidade específica.
     *
     * @param nome Nome da comunidade
     * @return Nome do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getOwner().getName();
    }

    /**
     * Retorna uma lista formatada com os membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getMembers();
    }

    /**
     * Adiciona um usuário como membro de uma comunidade existente.
     *
     * @param loginUsuario Login do usuário a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade não existir
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void adicionarComunidade(String loginUsuario, String nome) throws CommunityNotFoundException, UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        user.getProfile().setParticipanteComunidade(this.communities.get(nome));
        this.communities.get(nome).addMember(user);

        saveData();
    }

    /**
     * Lista todas as comunidades das quais um usuário participa.
     *
     * @param loginUsuario Login do usuário
     * @return String formatada com a lista de comunidades
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return GlobalFormatter.formatList(user.getProfile().getComunidadesParticipante());
    }

    /**
     * Envia uma mensagem para uma comunidade específica.
     *
     * @param loginUsuario Login do usuário remetente
     * @param nome Nome da comunidade destinatária
     * @param mensagem Conteúdo da mensagem
     * @throws UserNotFoundException Se o usuário remetente não for encontrado
     * @throws CommunityNotFoundException Se a comunidade não existir
     */
    public void enviarMensagem(String loginUsuario, String nome, String mensagem) throws UserNotFoundException, CommunityNotFoundException {
        User user = findUserByLogin(loginUsuario);
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }

        Message message = new Message(user, communities.get(nome), mensagem);
        this.communities.get(nome).sendMessage(message);

        saveData();
    }

    /**
     * Lê a próxima mensagem na fila de mensagens do usuário.
     *
     * @param loginUsuario Login do usuário que está lendo
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyMessagesException Se não houver mensagens para ler
     */
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
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {

        User usuario = findUserByLogin(sessaoId);
        User paquera = findUserByLogin(paqueraLogin);

        this.verficarInimizade(usuario, paquera);

        if (sessaoId.equals(paqueraLogin)) {
            throw new SelfRelationshipException("paquera");
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
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {


        User fa = findUserByLogin(sessaoId);
        User idolo = findUserByLogin(idoloLogin);

        this.verficarInimizade(fa, idolo);

        if (sessaoId.equals(idoloLogin)) {
            throw new SelfRelationshipException("fã");
        }

        if (fa.getProfile().getIdolos().contains(idolo)) {
            throw new UserAlreadyAddedException("ídolo");
        }

        fa.getProfile().getIdolos().add(idolo);
        idolo.getProfile().getFas().add(fa);
        saveData();
    }

    /**
     * Adiciona um usuário como inimigo de outro usuário no sistema (relação mútua).
     *
     * @param sessaoId Login do usuário que está adicionando o inimigo
     * @param inimigoLogin Login do usuário a ser adicionado como inimigo
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     * @throws SelfRelationshipException Se o usuário tentar se adicionar como próprio inimigo
     * @throws UserAlreadyAddedException Se o usuário já estiver na lista de inimigos
     */
    public void adicionarInimigo(String sessaoId, String inimigoLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {

        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);

        if (sessaoId.equals(inimigoLogin)) {
            throw new SelfRelationshipException("inimigo");
        }

        if (usuario.getProfile().getInimigos().contains(inimigo)) {
            throw new UserAlreadyAddedException("inimigo");
        }

        usuario.getProfile().getInimigos().add(inimigo);
        inimigo.getProfile().getInimigos().add(usuario);
        saveData();
    }

    /**
     * Verifica se existe relação de inimizade entre dois usuários.
     *
     * @param usuario Usuário principal a ser verificado
     * @param inimigo Usuário que pode ser inimigo
     * @throws UserNotFoundException Se algum dos usuários não for encontrado
     * @throws EnemyAlertException Se os usuários forem inimigos entre si
     */
    public void verficarInimizade(User usuario, User inimigo)
            throws UserNotFoundException, EnemyAlertException {

        if(usuario.getProfile().getInimigos().contains(inimigo)) {
            throw new EnemyAlertException(inimigo.getLogin());
        }
    }

    /**
     * Obtém a lista formatada de paqueras de um usuário.
     *
     * @param sessaoId Login do usuário
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
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
     * Remove completamente um usuário do sistema, incluindo todas as suas relações e participações.
     *
     * <p>Esta operação realiza as seguintes ações:</p>
     * <ol>
     *   <li>Remove o usuário das listas de inimigos de outros usuários</li>
     *   <li>Remove o usuário das listas de amigos de outros usuários</li>
     *   <li>Remove o usuário das listas de fãs de seus ídolos</li>
     *   <li>Remove o usuário das listas de ídolos de seus fãs</li>
     *   <li>Remove o usuário das listas de paqueras de outros usuários</li>
     *   <li>Remove o usuário de todas as comunidades que participa como membro</li>
     *   <li>Para comunidades onde é dono:
     *     <ul>
     *       <li>Remove todos os membros</li>
     *       <li>Remove a comunidade do sistema</li>
     *     </ul>
     *   </li>
     *   <li>Remove todos os recados enviados pelo usuário</li>
     *   <li>Limpa todos os dados do perfil do usuário</li>
     *   <li>Remove o usuário da lista de sessões ativas</li>
     *   <li>Remove o usuário da lista principal de usuários</li>
     * </ol>
     *
     * @param sessaoId Login do usuário a ser removido
     * @throws UserNotFoundException Se o usuário não for encontrado no sistema
     *
     * @see UserProfile#clear()
     * @see Community#removeMember(User)
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);

        // Remover o usuário das listas de INIMIGOS de outros usuários
        for (User inimigo : new ArrayList<>(usuario.getProfile().getInimigos())) {
            inimigo.getProfile().removerInimigo(usuario);
        }

        // Remover o usuário das listas de AMIGOS de outros usuários
        for (String amigo : new ArrayList<>(usuario.getProfile().getAmigos())) {
            User userAmigo = findUserByLogin(amigo);
            userAmigo.getProfile().removerAmigo(userAmigo.getName());
        }

        // Remover o usuário das listas de FÃS de seus ídolos
        for (User idolo : new ArrayList<>(usuario.getProfile().getIdolos())) {
            idolo.getProfile().removerIdolo(usuario);
        }

        // Remover o usuário das listas de ÍDOLOS de seus fãs
        for (User fa : new ArrayList<>(usuario.getProfile().getFas())) {
            fa.getProfile().removerFa(usuario);
        }

        // Remover o usuário das listas de PAQUERAS de outros usuários
        for (User paquera : new ArrayList<>(usuario.getProfile().getPaqueras())) {
            paquera.getProfile().removerPaquera(usuario);
        }

        // Remover o usuário dos membros das comunidades que participa
        for (Community comunidade : usuario.getProfile().getComunidadesParticipante()) {
            comunidade.removeMember(usuario);
        }

        // Processar comunidades onde o usuário é dono
        for (Community comunidade : usuario.getProfile().getComunidadesDono()) {
            for (User member : new ArrayList<>(comunidade.getMemberObject())) {
                comunidade.removeMember(member);
                member.getProfile().sairComunidade(comunidade);
            }
            this.communities.remove(comunidade.getName());
        }

        // Remover recados enviados pelo usuário
        for (User userRecado : new ArrayList<>(this.users)) {
            for (Note note : userRecado.getProfile().getRecados()) {
                if(note.getRemetente().equals(usuario)) {
                    userRecado.getProfile().removerRecado(note);
                }
            }
        }

        // Limpar dados do usuário e remover do sistema
        usuario.getProfile().clear();
        this.sessions.remove(usuario.getName());
        this.users.remove(usuario);

        saveData();
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