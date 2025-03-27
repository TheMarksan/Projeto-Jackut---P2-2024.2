package br.ufal.ic.p2.jackut.exceptions;

public class SessionOpeningException extends Exception {

    public SessionOpeningException(String message) {
        super(message);
    }

    public SessionOpeningException(String message, Throwable cause) {
        super(message, cause);
    }
}
