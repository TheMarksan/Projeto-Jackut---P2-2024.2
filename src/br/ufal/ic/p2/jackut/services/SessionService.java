package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.persistence.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para gestão de sessões de usuário no sistema Jackut.
 * Responsável por autenticar usuários, manter sessões ativas e persistir essas informações.
 */
public class SessionService {
    /** Objeto responsável pela persistência das sessões */
    private final SessionDAO sessionDAO;

    /** Serviço de usuários utilizado para autenticação */
    private final UserService userService;

    /** Lista de sessões ativas (representadas pelo login do usuário) */
    private List<String> activeSessions;

    /**
     * Construtor do serviço de sessões.
     *
     * @param sessionDAO DAO responsável pela persistência das sessões
     * @param userService Serviço de usuários para validação de login e senha
     */
    public SessionService(SessionDAO sessionDAO, UserService userService) {
        this.sessionDAO = sessionDAO;
        this.userService = userService;
        this.activeSessions = sessionDAO.load();
        if (activeSessions == null) activeSessions = new ArrayList<>();
    }

    /**
     * Abre uma nova sessão para o usuário autenticado.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da sessão (neste caso, o próprio login)
     * @throws SessionOpeningException Se login ou senha estiverem incorretos
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        if (login == null || senha == null) {
            throw new SessionOpeningException();
        }

        // Verifica se já existe sessão ativa para este usuário
        if (activeSessions.contains(login)) {
            return login;
        }

        for (User user : userService.getUsers()) {
            if (user != null && user.getName().equals(login)) {
                if (user.getPassword().equals(senha)) {
                    activeSessions.add(user.getLogin());
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
     * Verifica se uma sessão está ativa.
     *
     * @param sessionId ID da sessão a ser verificada
     * @return true se a sessão estiver ativa, false caso contrário
     */
    public boolean isSessionValid(String sessionId) {
        return sessionId != null && activeSessions.contains(sessionId);
    }

    /**
     * Encerra uma sessão ativa, removendo-a da lista.
     *
     * @param sessionId ID da sessão a ser encerrada
     */
    public void fecharSessao(String sessionId) {
        activeSessions.remove(sessionId);
        saveData();
    }

    /**
     * Remove todas as sessões ativas.
     * Útil para reinício do sistema ou logout geral.
     */
    public void limparSessoes() {
        activeSessions.clear();
        saveData();
    }

    /**
     * Salva a lista atual de sessões no armazenamento persistente.
     */
    private void saveData() {
        sessionDAO.save(activeSessions);
    }

    /**
     * Retorna a lista de sessões ativas.
     *
     * @return Lista contendo os logins dos usuários com sessões ativas
     */
    public List<String> getActiveSessions() {
        return activeSessions;
    }
}
