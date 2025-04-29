/**
 * Classe que representa o perfil de um usu�rio no sistema Jackut.
 * Cont�m informa��es pessoais do usu�rio, lista de amigos, solicita��es pendentes e mensagens.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.Community.CommunityCreationException;
import br.ufal.ic.p2.jackut.exceptions.Community.UserAlreadyMemberException;

import java.io.Serializable;
import java.util.*;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    // Mapa de atributos do perfil do usu�rio
    private Map<String, String> atributos;

    // Lista de amigos
    private List<String> amigos;
    private List<String> amigosPendentes;
    private Queue<Note> recados;
    private Queue<Message> mensagens;
    private List<Note> recadosLidos;
    private List<Community> comunidadesParticipante, comunidadesDono;

    /**
     * Construtor da classe UserProfile.
     * Inicializa as estruturas de dados do perfil do usu�rio.
     */
    public UserProfile() {
        this.atributos = new HashMap<>();
        this.amigos = new ArrayList<>();
        this.amigosPendentes = new ArrayList<>();
        this.recados = new LinkedList<>();
        this.recadosLidos = new ArrayList<>();
        this.comunidadesParticipante = new ArrayList<>();
        this.comunidadesDono = new ArrayList<>();
        this.mensagens = new LinkedList<>();
    }

    /**
     * Obt�m o valor de um atributo do perfil do usu�rio.
     *
     * @param chave Nome do atributo.
     * @return Valor do atributo ou uma string vazia se n�o existir.
     */
    public String getAtributo(String chave) {
        return atributos.getOrDefault(chave, "");
    }

    /**
     * Define um valor para um atributo do perfil do usu�rio.
     *
     * @param chave Nome do atributo.
     * @param valor Novo valor do atributo.
     * @return
     */
    public boolean setAtributo(String chave, String valor) {
        atributos.put(chave, valor);
        if (chave.equals("") || valor.equals(" ")) {
            return false;
        }
        return true;
    }

    /**
     * Verifica se um determinado atributo do perfil est� preenchido.
     *
     * @param chave Nome do atributo a ser verificado.
     * @return true se o atributo estiver preenchido, false caso contr�rio.
     */
    public boolean isAtributoPreenchido(String chave) {
        return atributos.containsKey(chave) && !atributos.get(chave).isEmpty();
    }

    /**
     * Obt�m a lista de amigos do usu�rio.
     *
     * @return Lista de logins dos amigos do usu�rio.
     */
    public List<String> getAmigos() {
        return amigos;
    }

    /**
     * Obt�m a lista de solicita��es de amizade pendentes.
     *
     * @return Lista de logins com solicita��es de amizade pendentes.
     */
    public List<String> getAmigosPendentes() {
        return amigosPendentes;
    }

    /**
     * Adiciona um novo amigo � lista de solicita��es pendentes.
     *
     * @param loginAmigo Login do usu�rio que enviou a solicita��o de amizade.
     */
    public void adicionarAmigoPendente(String loginAmigo) {
        if (!amigosPendentes.contains(loginAmigo)) {
            amigosPendentes.add(loginAmigo);
        }
    }

    /**
     * Remove um amigo da lista de solicita��es pendentes.
     *
     * @param loginAmigo Login do usu�rio a ser removido da lista de pendentes.
     */
    public void removerAmigoPendente(String loginAmigo) {
        amigosPendentes.remove(loginAmigo);
    }

    /**
     * Obt�m a lista de recados j� lidos pelo usu�rio.
     *
     * @return Lista de recados lidos.
     */
    public List<Note> getRecadosLidos() {
        return recadosLidos;
    }

    /**
     * Obt�m a fila de recados n�o lidos do usu�rio.
     *
     * @return Fila de recados n�o lidos.
     */
    public Queue<Note> getRecados() {
        return recados;
    }

    public void setDonoComunidades(Community comunidade) {
        if(this.comunidadesDono.contains(comunidade)){
            return;
        }
        this.comunidadesDono.add(comunidade);
    }

    public void setParticipanteComunidade(Community comunidade) throws UserAlreadyMemberException {
        if (this.comunidadesParticipante.contains(comunidade)) {
            throw new UserAlreadyMemberException();
        }

        this.comunidadesParticipante.add(comunidade);

    }

    public List<Community> getComunidadesParticipante() {
        return comunidadesParticipante;
    }

    public Queue<Message> getMensagens() {
        return mensagens;
    }

    public void setMensagens(Message mensagem) {
        this.mensagens.offer(mensagem);

    }
}
