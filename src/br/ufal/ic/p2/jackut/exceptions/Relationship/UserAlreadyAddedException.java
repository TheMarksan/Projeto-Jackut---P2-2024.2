package br.ufal.ic.p2.jackut.exceptions.Relationship;

public class UserAlreadyAddedException extends Exception {
    public UserAlreadyAddedException(String relationshipType)  {
        super("Usu�rio j� est� adicionado como " + relationshipType.toLowerCase() + ".");
    }
}
