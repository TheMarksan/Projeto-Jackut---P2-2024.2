package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.persistence.UserDAO;
import br.ufal.ic.p2.jackut.Utils.GlobalFormatter;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.exceptions.Message.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Note.*;
import br.ufal.ic.p2.jackut.exceptions.Relationship.*;

import java.util.*;

/**
 * Servi�o para gest�o de usu�rios, perfis, recados e relacionamentos no sistema Jackut.
 */
public class UserService {
    private final UserDAO userDAO;
    private List<User> users;

    /**
     * Constr�i um UserService com o UserDAO fornecido.
     *
     * @param userDAO DAO para persist�ncia de usu�rios
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.users = userDAO.load();
        if (users == null) users = new ArrayList<>();
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param nome Nome do usu�rio
     * @param senha Senha do usu�rio
     * @param login Login do usu�rio
     * @throws InvalidLoginException Se o login for inv�lido
     * @throws InvalidPasswordException Se a senha for inv�lida
     * @throws AccountAlreadyExistsException Se j� existir uma conta com o mesmo login ou nome
     */
    public void criarUsuario(String nome, String senha, String login)
            throws InvalidLoginException, InvalidPasswordException, AccountAlreadyExistsException {
        if (login == null || nome == null) {
            throw new InvalidLoginException();
        } else if (senha == null) {
            throw new InvalidPasswordException();
        }

        for (User user : users) {
            if (user.getName().equals(login) || user.getName().equals(nome)) {
                throw new AccountAlreadyExistsException();
            }
        }

        users.add(new User(nome, senha, login));
        saveData();
    }

    /**
     * Busca um usu�rio pelo login.
     *
     * @param login Login do usu�rio a ser encontrado
     * @return Usu�rio encontrado
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
     * Edita um atributo do perfil do usu�rio.
     *
     * @param id Login do usu�rio
     * @param atributo Atributo a ser editado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo for inv�lido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws InvalidAttributeException, UserNotFoundException {
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
     * Obt�m um atributo do usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Atributo a ser obtido
     * @return Valor do atributo
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws AttributeNotSetException Se o atributo n�o estiver definido
     * @throws InvalidAttributeException Se o atributo for inv�lido
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

    // ========== M�TODOS DE RELACIONAMENTOS ==========

    /**
     * Adiciona um amigo para o usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se ocorrer um erro na amizade
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o usu�rio j� for amigo
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException, SelfRelationshipException, UserAlreadyAddedException {

        User user = findUserByLogin(loginUsuario);
        User amigo = findUserByLogin(loginAmigo);

        verificarInimizade(user, amigo);

        if (loginUsuario.equals(loginAmigo)) throw new SelfRelationshipException("amigo");
        if (user.getProfile().getAmigosPendentes().contains(loginAmigo))
            throw FriendshipException.pendingFriendRequest();
        if (user.getProfile().getAmigos().contains(loginAmigo))
            throw new UserAlreadyAddedException("amigo");

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
     * Remove um amigo.
     *
     * @param loginUsuario Login do usu�rio
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se os usu�rios n�o forem amigos
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
     * Obt�m a lista de amigos formatada.
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
     * Obt�m todas as comunidades de um usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return String formatada com as comunidades
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getComunidadesUsuario(String loginUsuario) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return GlobalFormatter.formatList(user.getProfile().getComunidadesParticipante());
    }

    // ========== M�TODOS DE PAQUERAS ==========

    /**
     * Adiciona uma paquera para o usu�rio.
     *
     * @param sessaoId Login do usu�rio
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se a paquera j� foi adicionada
     * @throws SelfNoteException Se ocorrer um erro com recados
     */
    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {

        User usuario = findUserByLogin(sessaoId);
        User paquera = findUserByLogin(paqueraLogin);

        verificarInimizade(usuario, paquera);

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
     * Obt�m a lista de paqueras formatada.
     *
     * @param sessaoId Login do usu�rio
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        return GlobalFormatter.formatList(usuario.getProfile().getPaqueras());
    }

    // ========== M�TODOS DE F�S/�DOLOS ==========

    /**
     * Adiciona um �dolo para o usu�rio.
     *
     * @param sessaoId Login do f�
     * @param idoloLogin Login do �dolo
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o �dolo j� foi adicionado
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {

        User fa = findUserByLogin(sessaoId);
        User idolo = findUserByLogin(idoloLogin);

        verificarInimizade(fa, idolo);

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
     * Obt�m a lista de f�s formatada.
     *
     * @param loginIdolo Login do �dolo
     * @return String formatada com a lista de f�s
     * @throws UserNotFoundException Se o �dolo n�o for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        User idolo = findUserByLogin(loginIdolo);
        return GlobalFormatter.formatList(idolo.getProfile().getFas());
    }

    // ========== M�TODOS DE INIMIGOS ==========

    /**
     * Adiciona um inimigo para o usu�rio.
     *
     * @param sessaoId Login do usu�rio
     * @param inimigoLogin Login do inimigo
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o inimigo j� foi adicionado
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
     * Verifica se dois usu�rios s�o inimigos.
     *
     * @param sessaoId Login do primeiro usu�rio
     * @param inimigoLogin Login do segundo usu�rio
     * @return true se forem inimigos, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);
        return usuario.getProfile().getInimigos().contains(inimigo);
    }

    // ========== M�TODOS DE RECADOS ==========

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinat�rio
     * @param recado Texto do recado
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

        verificarInimizade(remetente, destinatario);

        Note note = new Note(remetente, destinatario, recado);
        destinatario.getProfile().getRecados().offer(note);
        saveData();
    }

    /**
     * L� o pr�ximo recado na fila de recados do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Recado lido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyNotesException Se n�o houver recados
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

    // ========== M�TODOS AUXILIARES ==========

    /**
     * Verifica se h� inimizade entre dois usu�rios.
     *
     * @param usuario Primeiro usu�rio
     * @param outroUsuario Segundo usu�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws EnemyAlertException Se os usu�rios forem inimigos
     */
    public void verificarInimizade(User usuario, User outroUsuario)
            throws UserNotFoundException, EnemyAlertException {

        if(usuario.getProfile().getInimigos().contains(outroUsuario)) {
            throw new EnemyAlertException(outroUsuario.getLogin());
        }
    }

    /**
     * L� a pr�xima mensagem na fila de mensagens do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Mensagem lida
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens
     */
    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        User user = findUserByLogin(loginUsuario);
        if(user.getProfile().getMensagens().isEmpty()) {
            throw new EmptyMessagesException();
        }
        Message mensagem = user.getProfile().getMensagens().poll();
        saveData();
        return mensagem;
    }

    /**
     * Verifica se um usu�rio tem paquera por outro.
     *
     * @param sessaoId Login do primeiro usu�rio
     * @param paqueraLogin Login do segundo usu�rio
     * @return true se houver paquera, false caso contr�rio
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
     * Remove completamente um usu�rio do sistema.
     *
     * @param sessaoId Login do usu�rio a ser removido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);

        // Remover das listas de inimigos
        for (User inimigo : new ArrayList<>(usuario.getProfile().getInimigos())) {
            inimigo.getProfile().removerInimigo(usuario);
        }

        // Remover das listas de amigos
        for (String amigo : new ArrayList<>(usuario.getProfile().getAmigos())) {
            User userAmigo = findUserByLogin(amigo);
            userAmigo.getProfile().removerAmigo(usuario.getName());
        }

        // Remover das listas de f�s de seus �dolos
        for (User idolo : new ArrayList<>(usuario.getProfile().getIdolos())) {
            idolo.getProfile().removerIdolo(usuario);
        }

        // Remover das listas de �dolos de seus f�s
        for (User fa : new ArrayList<>(usuario.getProfile().getFas())) {
            fa.getProfile().removerFa(usuario);
        }

        // Remover das listas de paqueras de outros usu�rios
        for (User paquera : new ArrayList<>(usuario.getProfile().getPaqueras())) {
            paquera.getProfile().removerPaquera(usuario);
        }

        // Remover o usu�rio dos membros das comunidades que participa
        for (Community comunidade : usuario.getProfile().getComunidadesParticipante()) {
            comunidade.removeMember(usuario);
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
        this.users.remove(usuario);
        saveData();
    }

    /**
     * Obt�m a lista de todos os usu�rios.
     *
     * @return Lista de usu�rios
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Salva os dados dos usu�rios no DAO.
     */
    private void saveData() {
        userDAO.save(users);
    }
}