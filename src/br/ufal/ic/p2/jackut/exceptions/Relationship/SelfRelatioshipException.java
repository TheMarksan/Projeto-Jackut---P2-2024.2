package br.ufal.ic.p2.jackut.exceptions.Relationship;

public class SelfRelatioshipException extends Exception {
    public SelfRelatioshipException(String relationshipType) {
        super(
               relationshipType.toLowerCase().equals("amizade") ? "Usuário não pode adicionar a si mesmo como amigo." : "Usuário não pode ser " + relationshipType.toLowerCase() + " de si mesmo."
        );
    }
}
