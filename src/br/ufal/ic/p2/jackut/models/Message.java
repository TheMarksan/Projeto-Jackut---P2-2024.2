package br.ufal.ic.p2.jackut.models;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private User remetente;
    private Community comunidade;
    private String messagem;

    public Message(User remetente, Community comunidade, String messagem) {
        this.remetente = remetente;
        this.comunidade = comunidade;
        this.messagem = messagem;
    }

    public User getRemetente() {
        return remetente;
    }

    public Community getComunidade() {
        return comunidade;
    }

    public String getMessagem() {
        return messagem;
    }

    @Override
    public String toString() {
        return messagem;
    }
}
