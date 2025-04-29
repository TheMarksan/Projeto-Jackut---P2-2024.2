package br.ufal.ic.p2.jackut.exceptions.Note;

public class SendNoteException extends Exception {
    public SendNoteException(String message) {
        super(message);
    }

    public SendNoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
