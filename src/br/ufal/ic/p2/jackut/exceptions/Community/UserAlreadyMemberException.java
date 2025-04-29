package br.ufal.ic.p2.jackut.exceptions.Community;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException(){
      super("Usuario já faz parte dessa comunidade.");
    }
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}
