package br.ufal.ic.p2.jackut.exceptions.Message;

public class EmptyMessagesException extends Exception {
    public EmptyMessagesException() {
        super("N�o h� recados.");
    }

    public EmptyMessagesException(String message) {
        super(message);
    }
}
