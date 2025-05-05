package br.ufal.ic.p2.jackut.exceptions.User;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Senha inválida.");
    }
    public InvalidPasswordException(String message) {
        super(message);
    }
}
