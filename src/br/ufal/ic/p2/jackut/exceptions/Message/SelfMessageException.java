package br.ufal.ic.p2.jackut.exceptions.Message;

public class SelfMessageException extends Exception {
    public SelfMessageException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }

    public SelfMessageException(String message) {
        super(message);
    }
}
