package br.ufal.ic.p2.jackut.exceptions.Community;

public class CommunityNotFoundException extends Exception {

    public CommunityNotFoundException()  {
        super("Comunidade n�o existe.");
    }
    public CommunityNotFoundException(String message) {
        super(message);
    }
}
