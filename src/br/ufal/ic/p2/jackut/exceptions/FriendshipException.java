package br.ufal.ic.p2.jackut.exceptions;

public class FriendshipException extends Exception {
    public FriendshipException(String message) {
        super(message);
    }

    public FriendshipException(String message, Throwable cause) {
        super(message, cause);
    }
}
