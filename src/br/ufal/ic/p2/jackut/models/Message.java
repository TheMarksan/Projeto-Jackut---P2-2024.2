package br.ufal.ic.p2.jackut.models;
import java.io.Serializable;

/**
 * Classe que representa uma mensagem enviada em uma comunidade.
 * <p>
 * A mensagem contém informações sobre o remetente, a comunidade destinatária
 * e o conteúdo da mensagem propriamente dito.
 * </p>
 *
 * <p>Esta classe é serializável para permitir armazenamento e transmissão.</p>
 */
public class Message implements Serializable {
    /**
     * Número de versão para controle de serialização.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Usuário remetente da mensagem.
     */
    private User remetente;

    /**
     * Comunidade destinatária da mensagem.
     */
    private Community comunidade;

    /**
     * Conteúdo textual da mensagem.
     */
    private String messagem;

    /**
     * Constrói uma nova mensagem com os dados fornecidos.
     *
     * @param remetente Usuário que está enviando a mensagem
     * @param comunidade Comunidade para qual a mensagem está sendo enviada
     * @param messagem Texto da mensagem
     */
    public Message(User remetente, Community comunidade, String messagem) {
        this.remetente = remetente;
        this.comunidade = comunidade;
        this.messagem = messagem;
    }

    /**
     * Obtém o remetente da mensagem.
     *
     * @return Objeto User representando o remetente
     */
    public User getRemetente() {
        return remetente;
    }

    /**
     * Obtém a comunidade destinatária da mensagem.
     *
     * @return Objeto Community representando a comunidade
     */
    public Community getComunidade() {
        return comunidade;
    }

    /**
     * Obtém o conteúdo textual da mensagem.
     *
     * @return String com o texto da mensagem
     */
    public String getMessagem() {
        return messagem;
    }

    /**
     * Retorna uma representação textual da mensagem (apenas o conteúdo).
     *
     * @return String contendo o texto da mensagem
     */
    @Override
    public String toString() {
        return messagem;
    }
}