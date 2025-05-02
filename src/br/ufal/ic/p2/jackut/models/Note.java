package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Classe que representa um recado (note) enviado entre usu�rios.
 * <p>
 * Um recado cont�m informa��es sobre o remetente, destinat�rio
 * e o conte�do textual do recado.
 * </p>
 *
 * <p>Implementa {@link Serializable} para permitir serializa��o dos objetos.</p>
 */
public class Note implements Serializable {
    /**
     * N�mero de vers�o para controle de serializa��o.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Usu�rio remetente do recado.
     */
    private User remetente;

    /**
     * Usu�rio destinat�rio do recado.
     */
    private User destinatario;

    /**
     * Conte�do textual do recado.
     */
    private String recado;

    /**
     * Constr�i um novo recado com os dados fornecidos.
     *
     * @param remetente Usu�rio que est� enviando o recado (n�o pode ser nulo)
     * @param destinatario Usu�rio que est� recebendo o recado (n�o pode ser nulo)
     * @param recado Texto do recado (n�o pode ser nulo ou vazio)
     */
    public Note(User remetente, User destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    /**
     * Obt�m o usu�rio remetente do recado.
     *
     * @return Objeto {@link User} representando o remetente
     */
    public User getRemetente() {
        return remetente;
    }

    /**
     * Obt�m o usu�rio destinat�rio do recado.
     *
     * @return Objeto {@link User} representando o destinat�rio
     */
    public User getDestinatario() {
        return destinatario;
    }

    /**
     * Obt�m o conte�do textual do recado.
     *
     * @return String contendo o texto do recado
     */
    public String getRecado() {
        return recado;
    }

    /**
     * Retorna uma representa��o textual do recado (apenas o conte�do).
     *
     * @return String contendo o texto do recado
     */
    @Override
    public String toString() {
        return recado;
    }
}