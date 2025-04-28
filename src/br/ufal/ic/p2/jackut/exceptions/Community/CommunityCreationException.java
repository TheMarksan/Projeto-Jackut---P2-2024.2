package br.ufal.ic.p2.jackut.exceptions.Community;

public class CommunityCreationException extends Exception {

    public CommunityCreationException() {
        super("Comunidade com esse nome já existe.");
    }

    public CommunityCreationException(String message) {
        super(message);
    }

}
