package br.ufal.ic.p2.jackut.exceptions;

public class SendMessageException extends Exception {
    public SendMessageException(String message) {
        super(message);
    }

    public SendMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
