package br.ufal.ic.p2.jackut.exceptions.Note;

public class EmptyNotesException extends Exception {
    public EmptyNotesException() {
        super("N�o h� recados.");
    }

    public EmptyNotesException(String message) {
        super(message);
    }
}
