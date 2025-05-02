/**
 * Classe que representa o perfil de um usu�rio no sistema Jackut.
 * Cont�m informa��es pessoais do usu�rio, lista de amigos, solicita��es pendentes e mensagens.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.Community.*;

import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa o perfil do usu�rio.
 *
 * <p>Esta classe � serializ�vel para permitir armazenamento e transmiss�o.</p>
 */

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    // Mapa de atributos do perfil do usu�rio
    private Map<String, String> atributos;

    // Lista de amigos
    private List<String> amigos;
    private List<String> amigosPendentes;
    private List<User> paqueras, fas, inimigos, idolos;
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
        this.paqueras = new ArrayList<>();
        this.fas = new ArrayList<>();
        this.inimigos = new ArrayList<>();
        this.idolos = new ArrayList<>();
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

    /**
     * Adiciona uma comunidade � lista de comunidades das quais o usu�rio � dono.
     * Se a comunidade j� estiver na lista, n�o faz nada.
     *
     * @param comunidade Comunidade a ser adicionada
     */
    public void setDonoComunidades(Community comunidade) {
        if(this.comunidadesDono.contains(comunidade)){
            return;
        }
        this.comunidadesDono.add(comunidade);
    }

    /**
     * Adiciona uma comunidade � lista de comunidades das quais o usu�rio participa.
     *
     * @param comunidade Comunidade a ser adicionada
     * @throws UserAlreadyMemberException Se o usu�rio j� for membro da comunidade
     */
    public void setParticipanteComunidade(Community comunidade) throws UserAlreadyMemberException {
        if (this.comunidadesParticipante.contains(comunidade)) {
            throw new UserAlreadyMemberException();
        }
        this.comunidadesParticipante.add(comunidade);
    }

    /**
     * Retorna a lista de comunidades das quais o usu�rio participa.
     *
     * @return Lista de comunidades como participante
     */
    public List<Community> getComunidadesParticipante() {
        return comunidadesParticipante;
    }

    /**
     * Retorna a lista de comunidades das quais o usu�rio � dono.
     *
     * @return Lista de comunidades como dono
     */
    public List<Community> getComunidadesDono() {
        return comunidadesDono;
    }

    /**
     * Retorna a fila de mensagens recebidas pelo usu�rio.
     *
     * @return Fila de mensagens n�o lidas
     */
    public Queue<Message> getMensagens() {
        return mensagens;
    }

    /**
     * Adiciona uma nova mensagem � fila de mensagens do usu�rio.
     *
     * @param mensagem Mensagem a ser adicionada
     */
    public void setMensagens(Message mensagem) {
        this.mensagens.offer(mensagem);
    }

    /**
     * Adiciona um novo recado � lista de recados do usu�rio.
     *
     * @param recado Recado a ser adicionado
     */
    public void setRecados(Note recado) {
        this.recados.offer(recado);
    }

    /**
     * Adiciona um usu�rio � lista de paqueras do usu�rio atual.
     *
     * @param paquera Usu�rio a ser adicionado como paquera
     */
    public void setPaquera(User paquera) {
        this.paqueras.add(paquera);
    }

    /**
     * Adiciona um usu�rio � lista de f�s do usu�rio atual.
     *
     * @param fa Usu�rio a ser adicionado como f�
     */
    public void setFas(User fa) {
        this.fas.add(fa);
    }

    /**
     * Adiciona um usu�rio � lista de inimigos do usu�rio atual.
     *
     * @param inimigo Usu�rio a ser adicionado como inimigo
     */
    public void setInimigos(User inimigo) {
        this.inimigos.add(inimigo);
    }

    /**
     * Adiciona um usu�rio � lista de �dolos do usu�rio atual.
     *
     * @param idolo Usu�rio a ser adicionado como �dolo
     */
    public void setIdolos(User idolo){
        this.idolos.add(idolo);
    }

    /**
     * Retorna a lista de paqueras do usu�rio.
     *
     * @return Lista de usu�rios paqueras
     */
    public List<User> getPaqueras() {
        return paqueras;
    }

    /**
     * Retorna a lista de f�s do usu�rio.
     *
     * @return Lista de usu�rios f�s
     */
    public List<User> getFas() {
        return fas;
    }

    /**
     * Retorna a lista de inimigos do usu�rio.
     *
     * @return Lista de usu�rios inimigos
     */
    public List<User> getInimigos() {
        return inimigos;
    }

    /**
     * Retorna a lista de �dolos do usu�rio.
     *
     * @return Lista de usu�rios �dolos
     */
    public List<User> getIdolos() {
        return idolos;
    }

    /**
     * Remove o usu�rio de uma comunidade da qual participa.
     *
     * @param comunidade Comunidade da qual o usu�rio vai sair
     */
    public void sairComunidade(Community comunidade) {
        this.comunidadesParticipante.remove(comunidade);
    }

    /**
     * Remove um recado espec�fico da lista de recados do usu�rio.
     *
     * @param recado Recado a ser removido
     */
    public void removerRecado(Note recado) {
        this.recados.remove(recado);
    }

    /**
     * Remove um amigo da lista de amigos pendentes.
     *
     * @param amigo Nome do amigo a ser removido
     */
    public void removerAmigo(String amigo) {
        this.amigosPendentes.remove(amigo);
    }

    /**
     * Remove um usu�rio da lista de inimigos.
     *
     * @param inimigo Usu�rio a ser removido dos inimigos
     */
    public void removerInimigo(User inimigo) {
        this.inimigos.remove(inimigo);
    }

    /**
     * Remove um usu�rio da lista de �dolos.
     *
     * @param idolo Usu�rio a ser removido dos �dolos
     */
    public void removerIdolo(User idolo) {
        this.idolos.remove(idolo);
    }

    /**
     * Remove um usu�rio da lista de f�s.
     *
     * @param fa Usu�rio a ser removido dos f�s
     */
    public void removerFa(User fa) {
        this.fas.remove(fa);
    }

    /**
     * Remove um usu�rio da lista de paqueras.
     *
     * @param paquera Usu�rio a ser removido das paqueras
     */
    public void removerPaquera(User paquera) {
        this.paqueras.remove(paquera);
    }

    /**
     * Limpa todos os dados de relacionamento e mensagens do usu�rio.
     * Inclui:
     * - Amigos pendentes e confirmados
     * - Inimigos
     * - �dolos
     * - Paqueras
     * - F�s
     * - Recados
     * - Comunidades
     * - Mensagens
     */
    public void clear() {
        this.amigosPendentes.clear();
        this.amigos.clear();
        this.inimigos.clear();
        this.idolos.clear();
        this.paqueras.clear();
        this.fas.clear();
        this.recadosLidos.clear();
        this.recados.clear();
        this.comunidadesParticipante.clear();
        this.comunidadesDono.clear();
        this.mensagens.clear();
    }
}
