package br.ufal.ic.p2.jackut.exceptions.User;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Senha inv�lida.");
    }
    public InvalidPasswordException(String message) {
        super(message);
    }
}
