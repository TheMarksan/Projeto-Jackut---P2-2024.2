package br.ufal.ic.p2.jackut.exceptions;

public class UserCreationException extends Exception {

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }


}
