package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.Utils.GlobalFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma comunidade no sistema.
 * <p>
 * Uma comunidade possui um nome, descrição, dono e lista de membros.
 * Permite o envio de mensagens para todos os membros da comunidade.
 * </p>
 *
 * <p>Implementa {@link Serializable} para permitir armazenamento persistente.</p>
 */
public class Community implements Serializable {
    /**
     * Número de versão para controle de serialização.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nome da comunidade.
     */
    private String name;

    /**
     * Descrição da comunidade.
     */
    private String description;

    /**
     * Usuário dono/criador da comunidade.
     */
    private User owner;

    /**
     * Lista de membros da comunidade.
     */
    private ArrayList<User> members;

    /**
     * Constrói uma nova comunidade com os dados básicos.
     *
     * @param name Nome da comunidade (não pode ser nulo ou vazio)
     * @param description Descrição da comunidade (não pode ser nulo)
     * @param owner Usuário criador/dono da comunidade (não pode ser nulo)
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
     * Obtém o nome da comunidade.
     *
     * @return Nome da comunidade
     */
    public String getName() {
        return name;
    }

    /**
     * Obtém a descrição da comunidade.
     *
     * @return Descrição da comunidade
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtém o dono da comunidade.
     *
     * @return Objeto {@link User} representando o dono
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Obtém a lista de membros formatada.
     *
     * @return String formatada com a lista de membros
     * @see GlobalFormatter#formatList(List)
     */
    public String getMembers() {
        return GlobalFormatter.formatList(members);
    }

    /**
     * Obtém a lista de objetos User dos membros.
     *
     * @return Lista de {@link User} membros da comunidade
     */
    public List<User> getMemberObject(){
        return members;
    }

    /**
     * Adiciona um novo membro à comunidade.
     *
     * @param user Usuário a ser adicionado (não pode ser nulo)
     */
    public void addMember(User user) {
        members.add(user);
    }

    /**
     * Remove um membro da comunidade.
     *
     * @param user Usuário a ser removido
     */
    public void removeMember(User user) {
        members.remove(user);
    }

    /**
     * Define/Substitui toda a lista de membros da comunidade.
     *
     * @param members Nova lista de membros (não pode ser nula)
     */
    public void setMembers(ArrayList<User> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    /**
     * Envia uma mensagem para todos os membros da comunidade.
     *
     * @param message Mensagem a ser enviada (não pode ser nula)
     */
    public void sendMessage(Message message) {
        for (User member : members) {
            member.getProfile().setMensagens(message);
        }
    }

    /**
     * Retorna uma representação textual da comunidade (apenas o nome).
     *
     * @return Nome da comunidade
     */
    @Override
    public String toString() {
        return this.getName();
    }
}