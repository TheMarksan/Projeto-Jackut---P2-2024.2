package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.Community.*;
import br.ufal.ic.p2.jackut.models.*;
import br.ufal.ic.p2.jackut.persistence.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;

import java.util.*;

/**
 * Servi�o para gest�o de comunidades no sistema Jackut.
 * Respons�vel por toda a l�gica de neg�cios relacionada a comunidades.
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *   <li>Cria��o e remo��o de comunidades</li>
 *   <li>Gest�o de membros das comunidades</li>
 *   <li>Envio de mensagens coletivas</li>
 *   <li>Consulta de informa��es sobre comunidades</li>
 * </ul>
 *
 * @author Marcos Melo
 * @version 1.0
 */
public class CommunityService {
    private final CommunityDAO communityDAO;
    private final UserService userService;
    private Map<String, Community> communities;

    /**
     * Construtor do servi�o de comunidades.
     *
     * @param communityDAO DAO para persist�ncia das comunidades
     * @param userService Servi�o de usu�rios para integra��o
     */
    public CommunityService(CommunityDAO communityDAO, UserService userService) {
        this.communityDAO = communityDAO;
        this.userService = userService;
        this.communities = communityDAO.load();
        if (communities == null) communities = new HashMap<>();
    }

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param loginUsuario Login do usu�rio que ser� o dono
     * @param nome Nome da comunidade (deve ser �nico)
     * @param descricao Descri��o da comunidade
     * @throws CommunityCreationException Se j� existir comunidade com mesmo nome
     * @throws UserNotFoundException Se o usu�rio dono n�o for encontrado
     */
    public void criarComunidade(String loginUsuario, String nome, String descricao)
            throws CommunityCreationException, UserNotFoundException {
        User dono = userService.findUserByLogin(loginUsuario);

        if(this.communities.containsKey(nome)) {
            throw new CommunityCreationException();
        }

        Community comunidade = new Community(nome, descricao, dono);
        this.communities.put(nome, comunidade);
        saveData();
    }

    /**
     * Obt�m a descri��o de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descri��o da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)) {
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getDescription();
    }

    /**
     * Obt�m o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)) {
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getOwner().getName();
    }

    /**
     * Obt�m a lista de membros de uma comunidade formatada.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        if(!this.communities.containsKey(nome)) {
            throw new CommunityNotFoundException();
        }
        return communities.get(nome).getMembers();
    }

    /**
     * Adiciona um usu�rio como membro de uma comunidade.
     *
     * @param loginUsuario Login do usu�rio a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void adicionarMembroComunidade(String loginUsuario, String nome)
            throws CommunityNotFoundException, UserNotFoundException {
        User user = userService.findUserByLogin(loginUsuario);

        if(!this.communities.containsKey(nome)) {
            throw new CommunityNotFoundException();
        }

        user.getProfile().setParticipanteComunidade(this.communities.get(nome));
        this.communities.get(nome).addMember(user);
        saveData();
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     *
     * @param loginUsuario Login do remetente
     * @param nome Nome da comunidade
     * @param mensagem Conte�do da mensagem
     * @throws UserNotFoundException Se o remetente n�o for encontrado
     * @throws CommunityNotFoundException Se a comunidade n�o existir
     */
    public void enviarMensagemComunidade(String loginUsuario, String nome, String mensagem)
            throws UserNotFoundException, CommunityNotFoundException {
        User user = userService.findUserByLogin(loginUsuario);

        if(!this.communities.containsKey(nome)) {
            throw new CommunityNotFoundException();
        }

        Message message = new Message(user, communities.get(nome), mensagem);
        this.communities.get(nome).sendMessage(message);
        saveData();
    }

    /**
     * Remove uma comunidade do sistema.
     *
     * @param comunidade Comunidade a ser removida
     */
    public void removerComunidade(Community comunidade) {
        this.communities.remove(comunidade.getName());
        saveData();
    }

    /**
     * Remove todas as comunidades onde o usu�rio � dono.
     *
     * @param loginUsuario Login do usu�rio/dono
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void deletarComunidadesDono(String loginUsuario) throws UserNotFoundException {
        User usuario = userService.findUserByLogin(loginUsuario);

        for (Community comunidade : usuario.getProfile().getComunidadesDono()) {
            for (User member : new ArrayList<>(comunidade.getMemberObject())) {
                comunidade.removeMember(member);
                member.getProfile().sairComunidade(comunidade);
            }
            this.communities.remove(comunidade.getName());
        }
    }

    /**
     * Limpa todas as comunidades do sistema (usado para reinicializa��o).
     */
    public void limparComunidades() {
        this.communities.clear();
    }

    /**
     * Obt�m o mapa de comunidades do sistema.
     *
     * @return Mapa de comunidades (nome -> comunidade)
     */
    public Map<String, Community> getCommunities() {
        return communities;
    }

    /**
     * Salva os dados das comunidades no sistema de persist�ncia.
     */
    private void saveData() {
        communityDAO.save(communities);
    }
}