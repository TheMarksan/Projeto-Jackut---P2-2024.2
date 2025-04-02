package br.ufal.ic.p2.jackut.exceptions.Profile;

public class InvalidAttributeException extends Exception {
    public InvalidAttributeException() {
        super("Atributo inv�lido.");
    }

    public InvalidAttributeException(String attribute) {
        super("O atributo '" + attribute + "' � inv�lido ou inexistente.");
    }
}