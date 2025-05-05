package br.ufal.ic.p2.jackut.exceptions.User;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("Login inválido.");
    }
    public InvalidLoginException(String message) {
        super(message);
    }
}
