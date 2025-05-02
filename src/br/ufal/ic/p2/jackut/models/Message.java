package br.ufal.ic.p2.jackut.models;
import java.io.Serializable;

/**
 * Classe que representa uma mensagem enviada em uma comunidade.
 * <p>
 * A mensagem cont�m informa��es sobre o remetente, a comunidade destinat�ria
 * e o conte�do da mensagem propriamente dito.
 * </p>
 *
 * <p>Esta classe � serializ�vel para permitir armazenamento e transmiss�o.</p>
 */
public class Message implements Serializable {
    /**
     * N�mero de vers�o para controle de serializa��o.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Usu�rio remetente da mensagem.
     */
    private User remetente;

    /**
     * Comunidade destinat�ria da mensagem.
     */
    private Community comunidade;

    /**
     * Conte�do textual da mensagem.
     */
    private String messagem;

    /**
     * Constr�i uma nova mensagem com os dados fornecidos.
     *
     * @param remetente Usu�rio que est� enviando a mensagem
     * @param comunidade Comunidade para qual a mensagem est� sendo enviada
     * @param messagem Texto da mensagem
     */
    public Message(User remetente, Community comunidade, String messagem) {
        this.remetente = remetente;
        this.comunidade = comunidade;
        this.messagem = messagem;
    }

    /**
     * Obt�m o remetente da mensagem.
     *
     * @return Objeto User representando o remetente
     */
    public User getRemetente() {
        return remetente;
    }

    /**
     * Obt�m a comunidade destinat�ria da mensagem.
     *
     * @return Objeto Community representando a comunidade
     */
    public Community getComunidade() {
        return comunidade;
    }

    /**
     * Obt�m o conte�do textual da mensagem.
     *
     * @return String com o texto da mensagem
     */
    public String getMessagem() {
        return messagem;
    }

    /**
     * Retorna uma representa��o textual da mensagem (apenas o conte�do).
     *
     * @return String contendo o texto da mensagem
     */
    @Override
    public String toString() {
        return messagem;
    }
}