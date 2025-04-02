package br.ufal.ic.p2.jackut.exceptions.Message;

public class EmptyMessagesException extends Exception {
    public EmptyMessagesException() {
        super("Não há recados.");
    }

    public EmptyMessagesException(String message) {
        super(message);
    }
}
