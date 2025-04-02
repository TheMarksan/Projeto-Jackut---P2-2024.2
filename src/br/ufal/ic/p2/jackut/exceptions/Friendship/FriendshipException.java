package br.ufal.ic.p2.jackut.exceptions.Friendship;

public class FriendshipException extends Exception {
    public FriendshipException() {
        super();
    }

    public static FriendshipException selfFriendship() {
        return new FriendshipException("Usuário não pode adicionar a si mesmo como amigo.");
    }

    public static FriendshipException pendingFriendRequest() {
        return new FriendshipException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }

    public static FriendshipException alreadyFriends() {
        return new FriendshipException("Usuário já está adicionado como amigo.");
    }

    public static FriendshipException notFriends() {
        return new FriendshipException("Usuário não está na sua lista de amigos.");
    }

    private FriendshipException(String message) {
        super(message);
    }
}

