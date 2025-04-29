package br.ufal.ic.p2.jackut.exceptions.Note;

public class EmptyNotesException extends Exception {
    public EmptyNotesException() {
        super("Não há recados.");
    }

    public EmptyNotesException(String message) {
        super(message);
    }
}
