/**
 * Classe que representa o perfil de um usuário no sistema Jackut.
 * Contém informações pessoais do usuário, lista de amigos, solicitações pendentes e mensagens.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.Community.*;

import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa o perfil do usuário.
 *
 * <p>Esta classe é serializável para permitir armazenamento e transmissão.</p>
 */

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    // Mapa de atributos do perfil do usuário
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
     * Inicializa as estruturas de dados do perfil do usuário.
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

    /**
     * Adiciona uma comunidade à lista de comunidades das quais o usuário é dono.
     * Se a comunidade já estiver na lista, não faz nada.
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
     * Adiciona uma comunidade à lista de comunidades das quais o usuário participa.
     *
     * @param comunidade Comunidade a ser adicionada
     * @throws UserAlreadyMemberException Se o usuário já for membro da comunidade
     */
    public void setParticipanteComunidade(Community comunidade) throws UserAlreadyMemberException {
        if (this.comunidadesParticipante.contains(comunidade)) {
            throw new UserAlreadyMemberException();
        }
        this.comunidadesParticipante.add(comunidade);
    }

    /**
     * Retorna a lista de comunidades das quais o usuário participa.
     *
     * @return Lista de comunidades como participante
     */
    public List<Community> getComunidadesParticipante() {
        return comunidadesParticipante;
    }

    /**
     * Retorna a lista de comunidades das quais o usuário é dono.
     *
     * @return Lista de comunidades como dono
     */
    public List<Community> getComunidadesDono() {
        return comunidadesDono;
    }

    /**
     * Retorna a fila de mensagens recebidas pelo usuário.
     *
     * @return Fila de mensagens não lidas
     */
    public Queue<Message> getMensagens() {
        return mensagens;
    }

    /**
     * Adiciona uma nova mensagem à fila de mensagens do usuário.
     *
     * @param mensagem Mensagem a ser adicionada
     */
    public void setMensagens(Message mensagem) {
        this.mensagens.offer(mensagem);
    }

    /**
     * Adiciona um novo recado à lista de recados do usuário.
     *
     * @param recado Recado a ser adicionado
     */
    public void setRecados(Note recado) {
        this.recados.offer(recado);
    }

    /**
     * Adiciona um usuário à lista de paqueras do usuário atual.
     *
     * @param paquera Usuário a ser adicionado como paquera
     */
    public void setPaquera(User paquera) {
        this.paqueras.add(paquera);
    }

    /**
     * Adiciona um usuário à lista de fãs do usuário atual.
     *
     * @param fa Usuário a ser adicionado como fã
     */
    public void setFas(User fa) {
        this.fas.add(fa);
    }

    /**
     * Adiciona um usuário à lista de inimigos do usuário atual.
     *
     * @param inimigo Usuário a ser adicionado como inimigo
     */
    public void setInimigos(User inimigo) {
        this.inimigos.add(inimigo);
    }

    /**
     * Adiciona um usuário à lista de ídolos do usuário atual.
     *
     * @param idolo Usuário a ser adicionado como ídolo
     */
    public void setIdolos(User idolo){
        this.idolos.add(idolo);
    }

    /**
     * Retorna a lista de paqueras do usuário.
     *
     * @return Lista de usuários paqueras
     */
    public List<User> getPaqueras() {
        return paqueras;
    }

    /**
     * Retorna a lista de fãs do usuário.
     *
     * @return Lista de usuários fãs
     */
    public List<User> getFas() {
        return fas;
    }

    /**
     * Retorna a lista de inimigos do usuário.
     *
     * @return Lista de usuários inimigos
     */
    public List<User> getInimigos() {
        return inimigos;
    }

    /**
     * Retorna a lista de ídolos do usuário.
     *
     * @return Lista de usuários ídolos
     */
    public List<User> getIdolos() {
        return idolos;
    }

    /**
     * Remove o usuário de uma comunidade da qual participa.
     *
     * @param comunidade Comunidade da qual o usuário vai sair
     */
    public void sairComunidade(Community comunidade) {
        this.comunidadesParticipante.remove(comunidade);
    }

    /**
     * Remove um recado específico da lista de recados do usuário.
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
     * Remove um usuário da lista de inimigos.
     *
     * @param inimigo Usuário a ser removido dos inimigos
     */
    public void removerInimigo(User inimigo) {
        this.inimigos.remove(inimigo);
    }

    /**
     * Remove um usuário da lista de ídolos.
     *
     * @param idolo Usuário a ser removido dos ídolos
     */
    public void removerIdolo(User idolo) {
        this.idolos.remove(idolo);
    }

    /**
     * Remove um usuário da lista de fãs.
     *
     * @param fa Usuário a ser removido dos fãs
     */
    public void removerFa(User fa) {
        this.fas.remove(fa);
    }

    /**
     * Remove um usuário da lista de paqueras.
     *
     * @param paquera Usuário a ser removido das paqueras
     */
    public void removerPaquera(User paquera) {
        this.paqueras.remove(paquera);
    }

    /**
     * Limpa todos os dados de relacionamento e mensagens do usuário.
     * Inclui:
     * - Amigos pendentes e confirmados
     * - Inimigos
     * - Ídolos
     * - Paqueras
     * - Fãs
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
