package br.ufal.ic.p2.jackut.exceptions.Relationship;

public class EnemyAlertException extends RuntimeException {
    public EnemyAlertException(String enemy) {
        super("Fun��o inv�lida: " + enemy + " � seu inimigo.");
    }
}
