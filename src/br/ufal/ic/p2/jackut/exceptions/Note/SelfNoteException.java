package br.ufal.ic.p2.jackut.exceptions.Note;

public class SelfNoteException extends Exception {
    public SelfNoteException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }

    public SelfNoteException(String message) {
        super(message);
    }
}
