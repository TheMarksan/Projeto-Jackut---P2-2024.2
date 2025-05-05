package br.ufal.ic.p2.jackut.exceptions.User;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException() {
        super("Conta com esse nome j� existe.");
    }
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
