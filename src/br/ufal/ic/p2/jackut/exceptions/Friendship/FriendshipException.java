package br.ufal.ic.p2.jackut.exceptions.Friendship;

public class FriendshipException extends Exception {
    public FriendshipException() {
        super();
    }

    public static FriendshipException selfFriendship() {
        return new FriendshipException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
    }

    public static FriendshipException pendingFriendRequest() {
        return new FriendshipException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }

    public static FriendshipException alreadyFriends() {
        return new FriendshipException("Usu�rio j� est� adicionado como amigo.");
    }

    public static FriendshipException notFriends() {
        return new FriendshipException("Usu�rio n�o est� na sua lista de amigos.");
    }

    private FriendshipException(String message) {
        super(message);
    }
}

