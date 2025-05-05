package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.persistence.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Servi�o para gest�o de sess�es de usu�rio no sistema Jackut.
 * Respons�vel por autenticar usu�rios, manter sess�es ativas e persistir essas informa��es.
 */
public class SessionService {
    /** Objeto respons�vel pela persist�ncia das sess�es */
    private final SessionDAO sessionDAO;

    /** Servi�o de usu�rios utilizado para autentica��o */
    private final UserService userService;

    /** Lista de sess�es ativas (representadas pelo login do usu�rio) */
    private List<String> activeSessions;

    /**
     * Construtor do servi�o de sess�es.
     *
     * @param sessionDAO DAO respons�vel pela persist�ncia das sess�es
     * @param userService Servi�o de usu�rios para valida��o de login e senha
     */
    public SessionService(SessionDAO sessionDAO, UserService userService) {
        this.sessionDAO = sessionDAO;
        this.userService = userService;
        this.activeSessions = sessionDAO.load();
        if (activeSessions == null) activeSessions = new ArrayList<>();
    }

    /**
     * Abre uma nova sess�o para o usu�rio autenticado.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o (neste caso, o pr�prio login)
     * @throws SessionOpeningException Se login ou senha estiverem incorretos
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        if (login == null || senha == null) {
            throw new SessionOpeningException();
        }

        // Verifica se j� existe sess�o ativa para este usu�rio
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
     * Verifica se uma sess�o est� ativa.
     *
     * @param sessionId ID da sess�o a ser verificada
     * @return true se a sess�o estiver ativa, false caso contr�rio
     */
    public boolean isSessionValid(String sessionId) {
        return sessionId != null && activeSessions.contains(sessionId);
    }

    /**
     * Encerra uma sess�o ativa, removendo-a da lista.
     *
     * @param sessionId ID da sess�o a ser encerrada
     */
    public void fecharSessao(String sessionId) {
        activeSessions.remove(sessionId);
        saveData();
    }

    /**
     * Remove todas as sess�es ativas.
     * �til para rein�cio do sistema ou logout geral.
     */
    public void limparSessoes() {
        activeSessions.clear();
        saveData();
    }

    /**
     * Salva a lista atual de sess�es no armazenamento persistente.
     */
    private void saveData() {
        sessionDAO.save(activeSessions);
    }

    /**
     * Retorna a lista de sess�es ativas.
     *
     * @return Lista contendo os logins dos usu�rios com sess�es ativas
     */
    public List<String> getActiveSessions() {
        return activeSessions;
    }
}
