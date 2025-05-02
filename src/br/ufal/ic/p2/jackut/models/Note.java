package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Classe que representa um recado (note) enviado entre usuários.
 * <p>
 * Um recado contém informações sobre o remetente, destinatário
 * e o conteúdo textual do recado.
 * </p>
 *
 * <p>Implementa {@link Serializable} para permitir serialização dos objetos.</p>
 */
public class Note implements Serializable {
    /**
     * Número de versão para controle de serialização.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Usuário remetente do recado.
     */
    private User remetente;

    /**
     * Usuário destinatário do recado.
     */
    private User destinatario;

    /**
     * Conteúdo textual do recado.
     */
    private String recado;

    /**
     * Constrói um novo recado com os dados fornecidos.
     *
     * @param remetente Usuário que está enviando o recado (não pode ser nulo)
     * @param destinatario Usuário que está recebendo o recado (não pode ser nulo)
     * @param recado Texto do recado (não pode ser nulo ou vazio)
     */
    public Note(User remetente, User destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    /**
     * Obtém o usuário remetente do recado.
     *
     * @return Objeto {@link User} representando o remetente
     */
    public User getRemetente() {
        return remetente;
    }

    /**
     * Obtém o usuário destinatário do recado.
     *
     * @return Objeto {@link User} representando o destinatário
     */
    public User getDestinatario() {
        return destinatario;
    }

    /**
     * Obtém o conteúdo textual do recado.
     *
     * @return String contendo o texto do recado
     */
    public String getRecado() {
        return recado;
    }

    /**
     * Retorna uma representação textual do recado (apenas o conteúdo).
     *
     * @return String contendo o texto do recado
     */
    @Override
    public String toString() {
        return recado;
    }
}