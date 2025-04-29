package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.Utils.GlobalFormatter;

import java.io.Serializable;
import java.util.ArrayList;


public class Community implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name, description;
    private User owner;
    private ArrayList<User> members;

    public Community(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }


    public User getOwner() {
        return owner;
    }
    public String getMembers() {
        return GlobalFormatter.formatList(members);
    }


    public void addMember(User user) {
        members.add(user);
    }
    public void removeMember(User user) {
        members.remove(user);
    }

    public void setMembers(ArrayList<User> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    public void sendMessage(Message message) {
        for (User member : members) {
            member.getProfile().setMensagens(message);
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
