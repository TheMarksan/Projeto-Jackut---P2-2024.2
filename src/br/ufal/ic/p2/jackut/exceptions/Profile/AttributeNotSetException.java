package br.ufal.ic.p2.jackut.exceptions.Profile;

public class AttributeNotSetException extends Exception {
    public AttributeNotSetException() {
        super("Atributo não preenchido.");
    }

    public AttributeNotSetException(String attribute) {
        super("O atributo '" + attribute + "' não foi preenchido.");
    }
}