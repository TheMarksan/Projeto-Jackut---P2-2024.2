package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.Utils.GlobalFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma comunidade no sistema.
 * <p>
 * Uma comunidade possui um nome, descri��o, dono e lista de membros.
 * Permite o envio de mensagens para todos os membros da comunidade.
 * </p>
 *
 * <p>Implementa {@link Serializable} para permitir armazenamento persistente.</p>
 */
public class Community implements Serializable {
    /**
     * N�mero de vers�o para controle de serializa��o.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nome da comunidade.
     */
    private String name;

    /**
     * Descri��o da comunidade.
     */
    private String description;

    /**
     * Usu�rio dono/criador da comunidade.
     */
    private User owner;

    /**
     * Lista de membros da comunidade.
     */
    private ArrayList<User> members;

    /**
     * Constr�i uma nova comunidade com os dados b�sicos.
     *
     * @param name Nome da comunidade (n�o pode ser nulo ou vazio)
     * @param description Descri��o da comunidade (n�o pode ser nulo)
     * @param owner Usu�rio criador/dono da comunidade (n�o pode ser nulo)
     */
    public Community(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.members = new ArrayList<>();
        this.owner = owner;

        this.members.add(owner);
        owner.getProfile().setDonoComunidades(this);
        owner.getProfile().setParticipanteComunidade(this);
    }

    /**
     * Obt�m o nome da comunidade.
     *
     * @return Nome da comunidade
     */
    public String getName() {
        return name;
    }

    /**
     * Obt�m a descri��o da comunidade.
     *
     * @return Descri��o da comunidade
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obt�m o dono da comunidade.
     *
     * @return Objeto {@link User} representando o dono
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Obt�m a lista de membros formatada.
     *
     * @return String formatada com a lista de membros
     * @see GlobalFormatter#formatList(List)
     */
    public String getMembers() {
        return GlobalFormatter.formatList(members);
    }

    /**
     * Obt�m a lista de objetos User dos membros.
     *
     * @return Lista de {@link User} membros da comunidade
     */
    public List<User> getMemberObject(){
        return members;
    }

    /**
     * Adiciona um novo membro � comunidade.
     *
     * @param user Usu�rio a ser adicionado (n�o pode ser nulo)
     */
    public void addMember(User user) {
        members.add(user);
    }

    /**
     * Remove um membro da comunidade.
     *
     * @param user Usu�rio a ser removido
     */
    public void removeMember(User user) {
        members.remove(user);
    }

    /**
     * Define/Substitui toda a lista de membros da comunidade.
     *
     * @param members Nova lista de membros (n�o pode ser nula)
     */
    public void setMembers(ArrayList<User> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    /**
     * Envia uma mensagem para todos os membros da comunidade.
     *
     * @param message Mensagem a ser enviada (n�o pode ser nula)
     */
    public void sendMessage(Message message) {
        for (User member : members) {
            member.getProfile().setMensagens(message);
        }
    }

    /**
     * Retorna uma representa��o textual da comunidade (apenas o nome).
     *
     * @return Nome da comunidade
     */
    @Override
    public String toString() {
        return this.getName();
    }
}