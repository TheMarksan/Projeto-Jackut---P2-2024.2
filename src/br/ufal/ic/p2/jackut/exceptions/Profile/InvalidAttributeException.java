package br.ufal.ic.p2.jackut.exceptions.Profile;

public class InvalidAttributeException extends Exception {
    public InvalidAttributeException() {
        super("Atributo inválido.");
    }

    public InvalidAttributeException(String attribute) {
        super("O atributo '" + attribute + "' é inválido ou inexistente.");
    }
}