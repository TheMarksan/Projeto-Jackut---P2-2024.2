package br.ufal.ic.p2.jackut.exceptions.Message;

public class EmptyMessagesException extends RuntimeException {
    public EmptyMessagesException()  {
        super("Não há mensagens.");
    }
    public EmptyMessagesException(String message) {
        super(message);
    }
}
