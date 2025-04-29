package br.ufal.ic.p2.jackut.exceptions.Community;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException(){
      super("Usuario j� faz parte dessa comunidade.");
    }
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}
