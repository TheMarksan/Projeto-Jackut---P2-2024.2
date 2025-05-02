package br.ufal.ic.p2.jackut.exceptions.Relationship;

public class UserAlreadyAddedException extends Exception {
    public UserAlreadyAddedException(String relationshipType)  {
        super("Usuário já está adicionado como " + relationshipType.toLowerCase() + ".");
    }
}
