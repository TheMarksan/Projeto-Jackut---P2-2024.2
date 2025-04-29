/**
 * Classe que representa o perfil de um usuário no sistema Jackut.
 * Contém informações pessoais do usuário, lista de amigos, solicitações pendentes e mensagens.
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

    // Mapa de atributos do perfil do usuário
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
     * Inicializa as estruturas de dados do perfil do usuário.
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
     * Obtém o valor de um atributo do perfil do usuário.
     *
     * @param chave Nome do atributo.
     * @return Valor do atributo ou uma string vazia se não existir.
     */
    public String getAtributo(String chave) {
        return atributos.getOrDefault(chave, "");
    }

    /**
     * Define um valor para um atributo do perfil do usuário.
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
     * Verifica se um determinado atributo do perfil está preenchido.
     *
     * @param chave Nome do atributo a ser verificado.
     * @return true se o atributo estiver preenchido, false caso contrário.
     */
    public boolean isAtributoPreenchido(String chave) {
        return atributos.containsKey(chave) && !atributos.get(chave).isEmpty();
    }

    /**
     * Obtém a lista de amigos do usuário.
     *
     * @return Lista de logins dos amigos do usuário.
     */
    public List<String> getAmigos() {
        return amigos;
    }

    /**
     * Obtém a lista de solicitações de amizade pendentes.
     *
     * @return Lista de logins com solicitações de amizade pendentes.
     */
    public List<String> getAmigosPendentes() {
        return amigosPendentes;
    }

    /**
     * Adiciona um novo amigo à lista de solicitações pendentes.
     *
     * @param loginAmigo Login do usuário que enviou a solicitação de amizade.
     */
    public void adicionarAmigoPendente(String loginAmigo) {
        if (!amigosPendentes.contains(loginAmigo)) {
            amigosPendentes.add(loginAmigo);
        }
    }

    /**
     * Remove um amigo da lista de solicitações pendentes.
     *
     * @param loginAmigo Login do usuário a ser removido da lista de pendentes.
     */
    public void removerAmigoPendente(String loginAmigo) {
        amigosPendentes.remove(loginAmigo);
    }

    /**
     * Obtém a lista de recados já lidos pelo usuário.
     *
     * @return Lista de recados lidos.
     */
    public List<Note> getRecadosLidos() {
        return recadosLidos;
    }

    /**
     * Obtém a fila de recados não lidos do usuário.
     *
     * @return Fila de recados não lidos.
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
