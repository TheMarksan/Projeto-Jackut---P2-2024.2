/**
 * Classe principal que implementa a l�gica de neg�cios do sistema Jackut.
 * Implementa o padr�o Singleton para garantir uma �nica inst�ncia.
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
    // Inst�ncia �nica do Singleton
    private static Sistema instance;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final CommunityDAO communityDAO;
    private List<User> users;
    private List<String> sessions;
    private Map<String, Community> communities;

    /**
     * Construtor privado para prevenir instancia��o externa.
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
     * M�todo est�tico para obter a inst�ncia �nica do Sistema.
     *
     * @return A inst�ncia �nica do Sistema
     */
    public static synchronized Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    /**
     * M�todo para reiniciar a inst�ncia �nica (�til para testes).
     */
    public static synchronized void resetInstance() {
        instance = new Sistema();
    }

    /**
     * Reinicia o sistema removendo todos os usu�rios e sess�es.
     */
    public void zerarSistema() {
        this.users = new ArrayList<>();
        this.sessions = new ArrayList<>();
        this.communities = new HashMap<>();
        saveData();
    }

    /**
     * Busca um usu�rio pelo login.
     *
     * @param login Login do usu�rio a ser encontrado
     * @return Objeto User correspondente
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
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
     * Cria um novo usu�rio no sistema.
     *
     * @param nome Nome completo do usu�rio
     * @param senha Senha de acesso
     * @param login Identificador �nico
     * @throws UserCreationException Se os dados forem inv�lidos ou o login j� existir
     */
    public void criarUsuario(String nome, String senha, String login) throws UserCreationException {
        if (login == null || nome == null) {
            throw new UserCreationException("Login inv�lido.");
        } else if (senha == null) {
            throw new UserCreationException("Senha inv�lida.");
        }

        for (User user : users) {
            if (user.getName().equals(login) || user.getName().equals(nome)) {
                throw new UserCreationException("Conta com esse nome j� existe.");
            }
        }

        users.add(new User(nome, senha, login));
        saveData();
    }

    /**
     * Autentica um usu�rio e inicia uma nova sess�o.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return Login da sess�o criada
     * @throws SessionOpeningException Se a autentica��o falhar
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
     * Edita um atributo do perfil do usu�rio.
     *
     * @param id Login do usu�rio
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo n�o existir
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
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

        User user = findUserByLogin(login);
        UserProfile profile = user.getProfile();

        // Atributos que n�o dependem do perfil
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
     * Adiciona um amigo para o usu�rio.
     *
     * @param loginUsuario Login do usu�rio que est� adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se j� forem amigos ou houver solicita��o pendente
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
     * Remove um amigo da lista de amigos do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se n�o forem amigos
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
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param loginUsuario Login do primeiro usu�rio
     * @param loginAmigo Login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return user.getProfile().getAmigos().contains(loginAmigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio formatada.
     *
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        User user = findUserByLogin(login);
        return GlobalFormatter.formatList(user.getProfile().getAmigos());
    }

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
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Conte�do do recado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyNotesException Se n�o houver mensagens para ler
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
     * Cria uma nova comunidade no sistema com o nome e descri��o especificados,
     * associando o usu�rio informado como dono e primeiro membro.
     *
     * @param loginUsuario Login do usu�rio que ser� o dono da comunidade
     * @param nome Nome da comunidade a ser criada (deve ser �nico)
     * @param descricao Descri��o da comunidade
     * @throws CommunityCreationException Se j� existir uma comunidade com o mesmo nome
     * @throws UserNotFoundException Se o usu�rio dono n�o for encontrado
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
     * Recupera a descri��o de uma comunidade existente.
     *
     * @param nome Nome da comunidade buscada
     * @return String com a descri��o da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getDescription();
    }

    /**
     * Obt�m o nome do dono de uma comunidade espec�fica.
     *
     * @param nome Nome da comunidade
     * @return Nome do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
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
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)){
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getMembers();
    }

    /**
     * Adiciona um usu�rio como membro de uma comunidade existente.
     *
     * @param loginUsuario Login do usu�rio a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
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
     * Lista todas as comunidades das quais um usu�rio participa.
     *
     * @param loginUsuario Login do usu�rio
     * @return String formatada com a lista de comunidades
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return GlobalFormatter.formatList(user.getProfile().getComunidadesParticipante());
    }

    /**
     * Envia uma mensagem para uma comunidade espec�fica.
     *
     * @param loginUsuario Login do usu�rio remetente
     * @param nome Nome da comunidade destinat�ria
     * @param mensagem Conte�do da mensagem
     * @throws UserNotFoundException Se o usu�rio remetente n�o for encontrado
     * @throws CommunityNotFoundException Se a comunidade n�o existir
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
     * L� a pr�xima mensagem na fila de mensagens do usu�rio.
     *
     * @param loginUsuario Login do usu�rio que est� lendo
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens para ler
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
     * Verifica se um usu�rio tem paquera por outro.
     *
     * @param sessaoId Login da sess�o do usu�rio
     * @param paqueraLogin Login da poss�vel paquera
     * @return true se for paquera, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User paquera = findUserByLogin(paqueraLogin);
        return usuario.getProfile().getPaqueras().contains(paquera);
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
        User idolo = findUserByLogin(idoloLogin);
        User fa = findUserByLogin(loginFa);
        return idolo.getProfile().getFas().contains(fa);
    }


    /**
     * Verifica se um usu�rio � inimigo de outro.
     *
     * @param sessaoId Login da sess�o do usu�rio
     * @param inimigoLogin Login do poss�vel inimigo
     * @return true se for inimigo, false caso contr�rio
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);
        return usuario.getProfile().getInimigos().contains(inimigo);
    }

    /**
     * Adiciona uma paquera para o usu�rio.
     *
     * @param sessaoId Login da sess�o do usu�rio
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
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
            this.enviarRecado(paquera.getName(), usuario.getName(), paquera.getLogin() + " � seu paquera - Recado do Jackut.");
            this.enviarRecado(usuario.getName(), paquera.getName(), usuario.getLogin() + " � seu paquera - Recado do Jackut.");
        }
        saveData();
    }


    /**
     * Adiciona um �dolo para o usu�rio (rela��o f�-�dolo).
     *
     * @param sessaoId Login da sess�o do f�
     * @param idoloLogin Login do �dolo
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {


        User fa = findUserByLogin(sessaoId);
        User idolo = findUserByLogin(idoloLogin);

        this.verficarInimizade(fa, idolo);

        if (sessaoId.equals(idoloLogin)) {
            throw new SelfRelationshipException("f�");
        }

        if (fa.getProfile().getIdolos().contains(idolo)) {
            throw new UserAlreadyAddedException("�dolo");
        }

        fa.getProfile().getIdolos().add(idolo);
        idolo.getProfile().getFas().add(fa);
        saveData();
    }

    /**
     * Adiciona um usu�rio como inimigo de outro usu�rio no sistema (rela��o m�tua).
     *
     * @param sessaoId Login do usu�rio que est� adicionando o inimigo
     * @param inimigoLogin Login do usu�rio a ser adicionado como inimigo
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     * @throws SelfRelationshipException Se o usu�rio tentar se adicionar como pr�prio inimigo
     * @throws UserAlreadyAddedException Se o usu�rio j� estiver na lista de inimigos
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
     * Verifica se existe rela��o de inimizade entre dois usu�rios.
     *
     * @param usuario Usu�rio principal a ser verificado
     * @param inimigo Usu�rio que pode ser inimigo
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     * @throws EnemyAlertException Se os usu�rios forem inimigos entre si
     */
    public void verficarInimizade(User usuario, User inimigo)
            throws UserNotFoundException, EnemyAlertException {

        if(usuario.getProfile().getInimigos().contains(inimigo)) {
            throw new EnemyAlertException(inimigo.getLogin());
        }
    }

    /**
     * Obt�m a lista formatada de paqueras de um usu�rio.
     *
     * @param sessaoId Login do usu�rio
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        return GlobalFormatter.formatList(usuario.getProfile().getPaqueras());
    }
    /**
     * Obt�m todos os f�s de um usu�rio.
     *
     * @param loginIdolo Login do �dolo
     * @return Conjunto de logins dos f�s
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        User idolo = findUserByLogin(loginIdolo);
        return GlobalFormatter.formatList(idolo.getProfile().getFas());
    }

    /**
     * Remove completamente um usu�rio do sistema, incluindo todas as suas rela��es e participa��es.
     *
     * <p>Esta opera��o realiza as seguintes a��es:</p>
     * <ol>
     *   <li>Remove o usu�rio das listas de inimigos de outros usu�rios</li>
     *   <li>Remove o usu�rio das listas de amigos de outros usu�rios</li>
     *   <li>Remove o usu�rio das listas de f�s de seus �dolos</li>
     *   <li>Remove o usu�rio das listas de �dolos de seus f�s</li>
     *   <li>Remove o usu�rio das listas de paqueras de outros usu�rios</li>
     *   <li>Remove o usu�rio de todas as comunidades que participa como membro</li>
     *   <li>Para comunidades onde � dono:
     *     <ul>
     *       <li>Remove todos os membros</li>
     *       <li>Remove a comunidade do sistema</li>
     *     </ul>
     *   </li>
     *   <li>Remove todos os recados enviados pelo usu�rio</li>
     *   <li>Limpa todos os dados do perfil do usu�rio</li>
     *   <li>Remove o usu�rio da lista de sess�es ativas</li>
     *   <li>Remove o usu�rio da lista principal de usu�rios</li>
     * </ol>
     *
     * @param sessaoId Login do usu�rio a ser removido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado no sistema
     *
     * @see UserProfile#clear()
     * @see Community#removeMember(User)
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);

        // Remover o usu�rio das listas de INIMIGOS de outros usu�rios
        for (User inimigo : new ArrayList<>(usuario.getProfile().getInimigos())) {
            inimigo.getProfile().removerInimigo(usuario);
        }

        // Remover o usu�rio das listas de AMIGOS de outros usu�rios
        for (String amigo : new ArrayList<>(usuario.getProfile().getAmigos())) {
            User userAmigo = findUserByLogin(amigo);
            userAmigo.getProfile().removerAmigo(userAmigo.getName());
        }

        // Remover o usu�rio das listas de F�S de seus �dolos
        for (User idolo : new ArrayList<>(usuario.getProfile().getIdolos())) {
            idolo.getProfile().removerIdolo(usuario);
        }

        // Remover o usu�rio das listas de �DOLOS de seus f�s
        for (User fa : new ArrayList<>(usuario.getProfile().getFas())) {
            fa.getProfile().removerFa(usuario);
        }

        // Remover o usu�rio das listas de PAQUERAS de outros usu�rios
        for (User paquera : new ArrayList<>(usuario.getProfile().getPaqueras())) {
            paquera.getProfile().removerPaquera(usuario);
        }

        // Remover o usu�rio dos membros das comunidades que participa
        for (Community comunidade : usuario.getProfile().getComunidadesParticipante()) {
            comunidade.removeMember(usuario);
        }

        // Processar comunidades onde o usu�rio � dono
        for (Community comunidade : usuario.getProfile().getComunidadesDono()) {
            for (User member : new ArrayList<>(comunidade.getMemberObject())) {
                comunidade.removeMember(member);
                member.getProfile().sairComunidade(comunidade);
            }
            this.communities.remove(comunidade.getName());
        }

        // Remover recados enviados pelo usu�rio
        for (User userRecado : new ArrayList<>(this.users)) {
            for (Note note : userRecado.getProfile().getRecados()) {
                if(note.getRemetente().equals(usuario)) {
                    userRecado.getProfile().removerRecado(note);
                }
            }
        }

        // Limpar dados do usu�rio e remover do sistema
        usuario.getProfile().clear();
        this.sessions.remove(usuario.getName());
        this.users.remove(usuario);

        saveData();
    }

    /**
     * Encerra o sistema, persisitindo os dados e limpando a sess�o.
     * Tamb�m limpa a inst�ncia Singleton.
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
            instance = null; // Limpa a inst�ncia Singleton
        }
    }

    /**
     * Salva os dados atuais no sistema de persist�ncia.
     */
    private void saveData() {
        userDAO.save(users);
        sessionDAO.save(sessions);
        communityDAO.save(communities);
    }
}