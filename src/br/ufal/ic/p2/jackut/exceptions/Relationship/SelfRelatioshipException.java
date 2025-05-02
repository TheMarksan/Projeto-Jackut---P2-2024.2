package br.ufal.ic.p2.jackut.exceptions.Relationship;

public class SelfRelatioshipException extends Exception {
    public SelfRelatioshipException(String relationshipType) {
        super(
               relationshipType.toLowerCase().equals("amizade") ? "Usu�rio n�o pode adicionar a si mesmo como amigo." : "Usu�rio n�o pode ser " + relationshipType.toLowerCase() + " de si mesmo."
        );
    }
}
