package br.ufal.ic.p2.jackut.exceptions.User;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usuário não cadastrado.");
    }

    public UserNotFoundException(String username) {
        super("O usuário '" + username + "' não está cadastrado.");
    }
}