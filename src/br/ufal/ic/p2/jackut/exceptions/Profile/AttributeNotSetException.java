package br.ufal.ic.p2.jackut.exceptions.Profile;

public class AttributeNotSetException extends Exception {
    public AttributeNotSetException() {
        super("Atributo n�o preenchido.");
    }

    public AttributeNotSetException(String attribute) {
        super("O atributo '" + attribute + "' n�o foi preenchido.");
    }
}