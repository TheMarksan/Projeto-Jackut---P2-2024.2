package br.ufal.ic.p2.jackut.exceptions.User;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usu�rio n�o cadastrado.");
    }

    public UserNotFoundException(String username) {
        super("O usu�rio '" + username + "' n�o est� cadastrado.");
    }
}