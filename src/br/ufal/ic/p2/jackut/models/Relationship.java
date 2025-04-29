package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

public class Relationship implements Serializable {
    private static final long serialVersionUID = 1L;
    private final User usuarioOrigem;
    private final User usuarioDestino;
    private final TipoRelacionamento tipo;
    private final Visibiliadade visibilidade;
    private final boolean reciproco;

    public Relationship(User usuarioOrigem, User usuarioDestino, TipoRelacionamento tipo, Visibiliadade visibilidade, boolean reciproco) {
        this.usuarioOrigem = usuarioOrigem;
        this.usuarioDestino = usuarioDestino;
        this.tipo = tipo;
        this.visibilidade = visibilidade;
        this.reciproco = reciproco;
    }

    public User getUsuarioOrigem() {
        return usuarioOrigem;
    }
    public User getUsuarioDestino() {
        return usuarioDestino;
    }
    public TipoRelacionamento getTipo() {
        return tipo;
    }
    public Visibiliadade getVisibilidade() {
        return visibilidade;
    }
    public boolean isReciproco() {
        return reciproco;
    }



    public enum TipoRelacionamento {
        AMIZADE(true),
        PAQUERA(false),
        FA_IDOLO(false),
        INIMIZADE(false);

        private final boolean reciprocoPadrao;
        TipoRelacionamento(boolean reciprocoPadrao) {
            this.reciprocoPadrao = reciprocoPadrao;
        }
    }

    public enum Visibiliadade {
        PUBLICO,
        PRIVADO,
    }
}
