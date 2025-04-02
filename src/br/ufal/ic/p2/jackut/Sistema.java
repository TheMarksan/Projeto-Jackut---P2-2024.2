/**
 * Classe principal que implementa a l�gica de neg�cios do sistema Jackut.
 * Respons�vel por gerenciar usu�rios, sess�es, amizades e mensagens.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.Friendship.*;
import br.ufal.ic.p2.jackut.exceptions.Message.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.persistence.*;

import java.util.*;

public class Sistema {
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private List<User> users;
    private List<String> sessions;

    /**
     * Construtor que inicializa o sistema carregando os dados persistidos.
     */
    public Sistema() {
        this.userDAO = new UserDAO();
        this.sessionDAO = new SessionDAO();
        this.users = userDAO.load();
        this.sessions = sessionDAO.load();

        if (users == null) users = new ArrayList<>();
        if (sessions == null) sessions = new ArrayList<>();
    }

    /**
     * Reinicia o sistema removendo todos os usu�rios e sess�es.
     */
    public void zerarSistema() {
        this.users = new ArrayList<>();
        this.sessions = new ArrayList<>();
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

        switch (atributo) {
            case "descricao": profile.setDescricao(valor); break;
            case "estadoCivil": profile.setEstadoCivil(valor); break;
            case "aniversario": profile.setAniversario(valor); break;
            case "filhos": profile.setFilhos(valor); break;
            case "idiomas": profile.setIdiomas(valor); break;
            case "cidadeNatal": profile.setCidadeNatal(valor); break;
            case "estilo": profile.setEstilo(valor); break;
            case "fumo": profile.setFumo(valor); break;
            case "bebo": profile.setBebo(valor); break;
            case "moro": profile.setMoro(valor); break;
            default: throw new InvalidAttributeException();
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
        if (user.getProfile() == null) {
            throw new AttributeNotSetException();
        }

        if (!user.getProfile().isAtributoPreenchido(atributo)) {
            throw new AttributeNotSetException();
        }

        switch (atributo) {
            case "nome": return user.getLogin();
            case "login": return user.getName();
            case "descricao": return user.getProfile().getDescricao();
            case "estadoCivil": return user.getProfile().getEstadoCivil();
            case "aniversario": return user.getProfile().getAniversario();
            case "filhos": return user.getProfile().getFilhos();
            case "idiomas": return user.getProfile().getIdiomas();
            case "cidadeNatal": return user.getProfile().getCidadeNatal();
            case "estilo": return user.getProfile().getEstilo();
            case "fumo": return user.getProfile().getFumo();
            case "bebo": return user.getProfile().getBebo();
            case "moro": return user.getProfile().getMoro();
            default: throw new InvalidAttributeException();
        }
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
        return "{" + String.join(",", user.getProfile().getAmigos()) + "}";
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinat�rio
     * @param recado Conte�do do recado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfMessageException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfMessageException {
        if (loginUsuario.equals(loginRecado)) {
            throw new SelfMessageException();
        }

        User destinatario = findUserByLogin(loginRecado);
        destinatario.getProfile().getRecados().offer(recado);
        saveData();
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Conte�do do recado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens para ler
     */
    public String lerRecado(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        User user = findUserByLogin(loginUsuario);
        if (user.getProfile().getRecados().isEmpty()) {
            throw new EmptyMessagesException();
        }
        String recado = user.getProfile().getRecados().poll();
        saveData();
        return recado;
    }

    /**
     * Encerra o sistema, persisitindo os dados e limpando a sess�o.
     */
    public void encerrarSistema() {
        try {
            // Garante que todos os dados sejam persistidos
            saveData();

            // Limpa as sess�es ativas (opcional, dependendo dos requisitos)
            sessions.clear();

            // Log de encerramento (�til para debug)
            System.out.println("Sistema Jackut encerrado com sucesso. Dados persistidos.");

        } catch (Exception e) {
            System.err.println("Erro ao encerrar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Salva os dados atuais no sistema de persist�ncia.
     */
    private void saveData() {
        userDAO.save(users);
        sessionDAO.save(sessions);
    }
}