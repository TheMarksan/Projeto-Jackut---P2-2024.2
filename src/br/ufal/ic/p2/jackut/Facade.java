/**
 * Fachada do sistema Jackut, responsável por gerenciar usuários e sessões.
 * Fornece métodos para criar usuários, abrir sessões, recuperar atributos e editar perfil.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.models.UserProfile;
import br.ufal.ic.p2.jackut.persistence.UserDAO;
import br.ufal.ic.p2.jackut.persistence.SessionDAO;
import br.ufal.ic.p2.jackut.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    public List<User> users;
    public List<String> sessions;

    /**
     * Construtor da fachada. Inicializa DAOs e carrega dados persistidos.
     */
    public Facade() {
        userDAO = new UserDAO();
        sessionDAO = new SessionDAO();

        // Carregar os dados de usuários e sessões
        this.users = userDAO.load();
        this.sessions = sessionDAO.load();

        // Se os dados não existirem, inicializa listas vazias
        if (users == null) users = new ArrayList<>();
        if (sessions == null) sessions = new ArrayList<>();
    }

    /**
     * Zera o sistema, removendo todos os usuários e sessões.
     */
    public void zerarSistema() {
        this.users = new ArrayList<>();
        this.sessions = new ArrayList<>();
        saveData();
    }

    /**
     * Obtém um atributo específico de um usuário pelo login.
     *
     * @param login O login do usuário.
     * @param atributo O atributo a ser recuperado ("nome", "login", "descricao", "estadoCivil", etc).
     * @return O valor do atributo solicitado ou "Atributo não preenchido." se estiver vazio.
     * @throws UserCreationException Se o usuário não for encontrado ou o atributo for inválido.
     */
    public String getAtributoUsuario(String login, String atributo) throws UserCreationException {
        for (User user : this.users) {
            if (user.getName().equals(login)) {
                if (user.getProfile() == null) {
                    throw new UserCreationException("Atributo não preenchido.");
                }

                if (!user.getProfile().isAtributoPreenchido(atributo)) {
                    throw new UserCreationException("Atributo não preenchido.");
                }

                switch (atributo) {
                    case "nome":
                        return user.getLogin();
                    case "login":
                        return user.getName();
                    case "descricao":
                        return user.getProfile().getDescricao();
                    case "estadoCivil":
                        return user.getProfile().getEstadoCivil();
                    case "aniversario":
                        return user.getProfile().getAniversario();
                    case "filhos":
                        return user.getProfile().getFilhos();
                    case "idiomas":
                        return user.getProfile().getIdiomas();
                    case "cidadeNatal":
                        return user.getProfile().getCidadeNatal();
                    case "estilo":
                        return user.getProfile().getEstilo();
                    case "fumo":
                        return user.getProfile().getFumo();
                    case "bebo":
                        return user.getProfile().getBebo();
                    case "moro":
                        return user.getProfile().getMoro();
                    default:
                        throw new UserCreationException("Atributo inválido.");
                }
            }
        }
        throw new UserCreationException("Usuário não cadastrado.");
    }




    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome O nome do usuário.
     * @param senha A senha do usuário.
     * @param login O login do usuário.
     * @throws UserCreationException Se o login, nome ou senha forem inválidos, ou se o usuário já existir.
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

        User user = new User(nome, senha, login);
        users.add(user);
        saveData();
    }

    /**
     * Abre uma sessão para um usuário autenticado.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @throws SessionOpeningException Se o login ou senha forem inválidos.
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException {
        if (login == null || senha == null) {
            throw new SessionOpeningException("Login ou senha inválidos.");
        }

        // Verifica se o login já está em uma sessão ativa
        for (String session : sessions) {
            if (session.equals(login)) {
                return login;  // Se já houver sessão aberta para esse login, retorna o login
            }
        }

        // Caso não tenha sessão aberta, tenta abrir uma nova
        for (User user : users) {
            if (user != null && user.getName().equals(login)) {
                if (user.getPassword().equals(senha)) {
                    sessions.add(user.getLogin());  // Adiciona o login à sessão
                    saveData();
                    return login;  // Retorna o login do usuário que abriu a sessão
                } else {
                    throw new SessionOpeningException("Login ou senha inválidos.");
                }
            }
        }

        throw new SessionOpeningException("Login ou senha inválidos.");
    }


    /**
     * Encerra o sistema, salvando os dados atuais.
     */
    public void encerrarSistema() {
        saveData();
    }

    /**
     * Salva os dados de usuários e sessões no sistema de persistência.
     */
    private void saveData() {
        userDAO.save(users);
        sessionDAO.save(sessions);
    }

    /**
     * Edita o perfil de um usuário.
     * Permite atualizar informações como descrição, estado civil, etc.
     *
     * @param id O login do usuário.
     * @param atributo O atributo que será alterado.
     * @param valor O valor atribuído.
     * @throws UserCreationException Se o usuário não for encontrado.
     */
    public void editarPerfil(String id, String atributo, String valor) throws UserCreationException {
        for (User user : this.users) {
            if (user.getName().equals(id)) {
                UserProfile profile = user.getProfile();
                if (profile == null) {
                    profile = new UserProfile();
                    user.setProfile(profile);
                }

                // Atualiza o atributo com o valor fornecido
                switch (atributo) {
                    case "descricao":
                        profile.setDescricao(valor);
                        break;
                    case "estadoCivil":
                        profile.setEstadoCivil(valor);
                        break;
                    case "aniversario":
                        profile.setAniversario(valor);
                        break;
                    case "filhos":
                        profile.setFilhos(valor);
                        break;
                    case "idiomas":
                        profile.setIdiomas(valor);
                        break;
                    case "cidadeNatal":
                        profile.setCidadeNatal(valor);
                        break;
                    case "estilo":
                        profile.setEstilo(valor);
                        break;
                    case "fumo":
                        profile.setFumo(valor);
                        break;
                    case "bebo":
                        profile.setBebo(valor);
                        break;
                    case "moro":
                        profile.setMoro(valor);
                        break;
                    default:
                        throw new UserCreationException("Atributo inválido.");
                }

                // Salva os dados após a atualização
                saveData();
                return;
            }
        }

        throw new UserCreationException("Usuário não cadastrado.");
    }

    // Métodos de amigos

    public void adicionarAmigo(String loginUsuario, String loginAmigo) throws UserCreationException, FriendshipException {
        User user = findUserByLogin(loginUsuario);
        User amigo = findUserByLogin(loginAmigo);

        if (loginUsuario.equals(loginAmigo)) {
            throw new FriendshipException("Usuário não pode adicionar a si mesmo como amigo.");
        }

        if (user.getProfile().getAmigosPendentes().contains(loginAmigo)) {
            throw new FriendshipException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        if (user.getProfile().getAmigos().contains(loginAmigo)) {
            throw new FriendshipException("Usuário já está adicionado como amigo.");
        }

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

    public void removerAmigo(String loginUsuario, String loginAmigo) throws UserCreationException, FriendshipException {
        User user = findUserByLogin(loginUsuario);
        User amigo = findUserByLogin(loginAmigo);

        if (!user.getProfile().getAmigos().contains(loginAmigo)) {
            throw new FriendshipException("Usuário não está na sua lista de amigos.");
        }

        user.getProfile().getAmigos().remove(loginAmigo);
        amigo.getProfile().getAmigos().remove(loginUsuario);
        saveData();
    }

    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserCreationException {
        User user = findUserByLogin(loginUsuario);
        return user.getProfile().getAmigos().contains(loginAmigo);
    }

    public String getAmigos(String login) throws UserCreationException {
        User user = findUserByLogin(login);
        List<String> amigos = user.getProfile().getAmigos();

        // Printar no formato desejado
        String listaAmigos = ("{" + String.join(",", amigos) + "}");

        return listaAmigos;
    }


    private User findUserByLogin(String login) throws UserCreationException {
        for (User user : users) {
            if (user.getName().equals(login)) {
                return user;
            }
        }
        throw new UserCreationException("Usuário não cadastrado.");
    }

}
