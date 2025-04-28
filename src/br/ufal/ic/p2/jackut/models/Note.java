package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;

    private User remetente;
    private User destinatario;
    private String recado;

    public Note(User remetente, User destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    public User getRemetente() {
        return remetente;
    }
    public User getDestinatario() {
        return destinatario;
    }
    public String getRecado() {
        return recado;
    }

    @Override
    public String toString() {
        return recado;
    }
}
