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
 * Serviço para gestão de usuários, perfis, recados e relacionamentos no sistema Jackut.
 */
public class UserService {
    private final UserDAO userDAO;
    private List<User> users;

    /**
     * Constrói um UserService com o UserDAO fornecido.
     *
     * @param userDAO DAO para persistência de usuários
     */
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.users = userDAO.load();
        if (users == null) users = new ArrayList<>();
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome Nome do usuário
     * @param senha Senha do usuário
     * @param login Login do usuário
     * @throws InvalidLoginException Se o login for inválido
     * @throws InvalidPasswordException Se a senha for inválida
     * @throws AccountAlreadyExistsException Se já existir uma conta com o mesmo login ou nome
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
     * Busca um usuário pelo login.
     *
     * @param login Login do usuário a ser encontrado
     * @return Usuário encontrado
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
     * Edita um atributo do perfil do usuário.
     *
     * @param id Login do usuário
     * @param atributo Atributo a ser editado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo for inválido
     * @throws UserNotFoundException Se o usuário não for encontrado
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
     * Obtém um atributo do usuário.
     *
     * @param login Login do usuário
     * @param atributo Atributo a ser obtido
     * @return Valor do atributo
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws AttributeNotSetException Se o atributo não estiver definido
     * @throws InvalidAttributeException Se o atributo for inválido
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

    // ========== MÉTODOS DE RELACIONAMENTOS ==========

    /**
     * Adiciona um amigo para o usuário.
     *
     * @param loginUsuario Login do usuário
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se ocorrer um erro na amizade
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o usuário já for amigo
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
     * @param loginUsuario Login do usuário
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se os usuários não forem amigos
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
     * Obtém a lista de amigos formatada.
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
     * Obtém todas as comunidades de um usuário.
     *
     * @param loginUsuario Login do usuário
     * @return String formatada com as comunidades
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getComunidadesUsuario(String loginUsuario) throws UserNotFoundException {
        User user = findUserByLogin(loginUsuario);
        return GlobalFormatter.formatList(user.getProfile().getComunidadesParticipante());
    }

    // ========== MÉTODOS DE PAQUERAS ==========

    /**
     * Adiciona uma paquera para o usuário.
     *
     * @param sessaoId Login do usuário
     * @param paqueraLogin Login da paquera
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se a paquera já foi adicionada
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
            this.enviarRecado(paquera.getName(), usuario.getName(), paquera.getLogin() + " é seu paquera - Recado do Jackut.");
            this.enviarRecado(usuario.getName(), paquera.getName(), usuario.getLogin() + " é seu paquera - Recado do Jackut.");
        }
        saveData();
    }

    /**
     * Obtém a lista de paqueras formatada.
     *
     * @param sessaoId Login do usuário
     * @return String formatada com a lista de paqueras
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        return GlobalFormatter.formatList(usuario.getProfile().getPaqueras());
    }

    // ========== MÉTODOS DE FÃS/ÍDOLOS ==========

    /**
     * Adiciona um ídolo para o usuário.
     *
     * @param sessaoId Login do fã
     * @param idoloLogin Login do ídolo
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o ídolo já foi adicionado
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {

        User fa = findUserByLogin(sessaoId);
        User idolo = findUserByLogin(idoloLogin);

        verificarInimizade(fa, idolo);

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
     * Obtém a lista de fãs formatada.
     *
     * @param loginIdolo Login do ídolo
     * @return String formatada com a lista de fãs
     * @throws UserNotFoundException Se o ídolo não for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        User idolo = findUserByLogin(loginIdolo);
        return GlobalFormatter.formatList(idolo.getProfile().getFas());
    }

    // ========== MÉTODOS DE INIMIGOS ==========

    /**
     * Adiciona um inimigo para o usuário.
     *
     * @param sessaoId Login do usuário
     * @param inimigoLogin Login do inimigo
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o inimigo já foi adicionado
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
     * Verifica se dois usuários são inimigos.
     *
     * @param sessaoId Login do primeiro usuário
     * @param inimigoLogin Login do segundo usuário
     * @return true se forem inimigos, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        User usuario = findUserByLogin(sessaoId);
        User inimigo = findUserByLogin(inimigoLogin);
        return usuario.getProfile().getInimigos().contains(inimigo);
    }

    // ========== MÉTODOS DE RECADOS ==========

    /**
     * Envia um recado para outro usuário.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinatário
     * @param recado Texto do recado
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

        verificarInimizade(remetente, destinatario);

        Note note = new Note(remetente, destinatario, recado);
        destinatario.getProfile().getRecados().offer(note);
        saveData();
    }

    /**
     * Lê o próximo recado na fila de recados do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Recado lido
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyNotesException Se não houver recados
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

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Verifica se há inimizade entre dois usuários.
     *
     * @param usuario Primeiro usuário
     * @param outroUsuario Segundo usuário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws EnemyAlertException Se os usuários forem inimigos
     */
    public void verificarInimizade(User usuario, User outroUsuario)
            throws UserNotFoundException, EnemyAlertException {

        if(usuario.getProfile().getInimigos().contains(outroUsuario)) {
            throw new EnemyAlertException(outroUsuario.getLogin());
        }
    }

    /**
     * Lê a próxima mensagem na fila de mensagens do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Mensagem lida
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyMessagesException Se não houver mensagens
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
     * Verifica se um usuário tem paquera por outro.
     *
     * @param sessaoId Login do primeiro usuário
     * @param paqueraLogin Login do segundo usuário
     * @return true se houver paquera, false caso contrário
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
     * Remove completamente um usuário do sistema.
     *
     * @param sessaoId Login do usuário a ser removido
     * @throws UserNotFoundException Se o usuário não for encontrado
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

        // Remover das listas de fãs de seus ídolos
        for (User idolo : new ArrayList<>(usuario.getProfile().getIdolos())) {
            idolo.getProfile().removerIdolo(usuario);
        }

        // Remover das listas de ídolos de seus fãs
        for (User fa : new ArrayList<>(usuario.getProfile().getFas())) {
            fa.getProfile().removerFa(usuario);
        }

        // Remover das listas de paqueras de outros usuários
        for (User paquera : new ArrayList<>(usuario.getProfile().getPaqueras())) {
            paquera.getProfile().removerPaquera(usuario);
        }

        // Remover o usuário dos membros das comunidades que participa
        for (Community comunidade : usuario.getProfile().getComunidadesParticipante()) {
            comunidade.removeMember(usuario);
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
        this.users.remove(usuario);
        saveData();
    }

    /**
     * Obtém a lista de todos os usuários.
     *
     * @return Lista de usuários
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Salva os dados dos usuários no DAO.
     */
    private void saveData() {
        userDAO.save(users);
    }
}