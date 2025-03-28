package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.persistence.UserDAO;
import br.ufal.ic.p2.jackut.persistence.SessionDAO;
import br.ufal.ic.p2.jackut.exceptions.SessionOpeningException;
import br.ufal.ic.p2.jackut.exceptions.UserCreationException;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    public List<User> users;
    public List<String> sessions;

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

    public void zerarSistema() {
        this.users = new ArrayList<>();
        this.sessions = new ArrayList<>();
        saveData();
    }

    public String getAtributoUsuario(String login, String atributo) throws UserCreationException {
        for (User user : this.users) {
            if (user.getName().equals(login)) {
                switch (atributo.toLowerCase()) {
                    case "nome":
                        return user.getLogin();
                    case "login":
                        return user.getName();
                    default:
                        throw new UserCreationException("Atributo inválido.");
                }
            }
        }
        throw new UserCreationException("Usuário não cadastrado.");
    }

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

    public void abrirSessao(String login, String senha) throws SessionOpeningException {
        if (login == null || senha == null) {
            throw new SessionOpeningException("Login ou senha inválidos.");
        }

        for (User user : users) {
            if (user != null && user.getName().equals(login)) {
                if (user.getPassword().equals(senha)) {
                    sessions.add(user.getLogin());
                    saveData();
                    return;
                } else {
                    throw new SessionOpeningException("Login ou senha inválidos.");
                }
            }
        }

        throw new SessionOpeningException("Login ou senha inválidos.");
    }

    public void encerrarSistema() {
        saveData();
    }

    private void saveData() {
        // Salva os dados de usuários e sessões usando os DAOs
        userDAO.save(users);
        sessionDAO.save(sessions);
    }
}
