package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.Community.CommunityCreationException;
import br.ufal.ic.p2.jackut.exceptions.Community.CommunityNotFoundException;
import br.ufal.ic.p2.jackut.exceptions.Relationship.*;
import br.ufal.ic.p2.jackut.exceptions.Message.EmptyMessagesException;
import br.ufal.ic.p2.jackut.exceptions.Note.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;
import br.ufal.ic.p2.jackut.models.*;

/**
 * Fachada principal do sistema Jackut que fornece a interface pública para interação com o sistema.
 * Atua como um ponto único de acesso para todas as operações do sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */
public class Facade {
    private final Sistema sistema;

    /**
     * Construtor que inicializa a fachada criando uma nova instância do sistema.
     */
    public Facade() {
        this.sistema = Sistema.getInstance();
    }

    /**
     * Reinicia o sistema removendo todos os usuários e sessões.
     */
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    /**
     * Obtém o valor de um atributo específico do usuário.
     *
     * @param login Login do usuário
     * @param atributo Atributo a ser consultado
     * @return Valor do atributo solicitado
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws AttributeNotSetException Se o atributo não estiver definido
     * @throws InvalidAttributeException Se o atributo não existir
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UserNotFoundException, AttributeNotSetException, InvalidAttributeException {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param nome Nome completo do usuário
     * @param senha Senha de acesso
     * @param login Identificador único
     * @throws UserCreationException Se os dados forem inválidos ou o login já existir
     */
    public void criarUsuario(String nome, String senha, String login) throws UserCreationException {
        sistema.criarUsuario(nome, senha, login);
    }

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return Login da sessão criada
     * @throws SessionOpeningException Se a autenticação falhar
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException, UserNotFoundException {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Encerra o sistema, salvando os dados atuais.
     */
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    /**
     * Edita um atributo do perfil do usuário.
     *
     * @param id Login do usuário
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo não existir
     * @throws ProfileEditException Se ocorrer erro na edição do perfil
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws InvalidAttributeException, ProfileEditException, UserNotFoundException {
        try {
            sistema.editarPerfil(id, atributo, valor);
        } catch (Exception e) {
            throw new ProfileEditException(e.getMessage());
        }
    }

    /**
     * Adiciona um amigo para o usuário.
     *
     * @param loginUsuario Login do usuário que está adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se já forem amigos ou houver solicitação pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        sistema.adicionarAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Remove um amigo da lista de amigos do usuário.
     *
     * @param loginUsuario Login do usuário
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws FriendshipException Se não forem amigos
     */
    public void removerAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        sistema.removerAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param loginUsuario Login do primeiro usuário
     * @param loginAmigo Login do segundo usuário
     * @return true se forem amigos, false caso contrário
     * @throws UserNotFoundException Se algum usuário não for encontrado
     */
    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserNotFoundException {
        return sistema.ehAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Obtém a lista de amigos de um usuário formatada.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinatário
     * @param recado Conteúdo do recado
     * @throws UserNotFoundException Se algum usuário não for encontrado
     * @throws SelfNoteException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfNoteException {
        sistema.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * Lê o próximo recado não lido do usuário.
     *
     * @param loginUsuario Login do usuário
     * @return Conteúdo do recado
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws EmptyNotesException Se não houver mensagens para ler
     */
    public Note lerRecado(String loginUsuario) throws UserNotFoundException, EmptyNotesException {
        return sistema.lerRecado(loginUsuario);
    }

    public void criarComunidade(String loginUsuario, String nome, String descricao) throws CommunityCreationException, UserNotFoundException{
        sistema.criarComunidade(loginUsuario, nome, descricao);
    }

    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
       return sistema.getDescricaoComunidade(nome);
    }

    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getDonoComunidade(nome);
    }

    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getMembrosComunidade(nome);
    }

    public void adicionarComunidade(String loginUsuario, String nome) throws CommunityNotFoundException, UserNotFoundException {
        sistema.adicionarComunidade(loginUsuario, nome);
    }

    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        return sistema.getComunidades(loginUsuario);
    }

    public void enviarMensagem(String loginUsuario, String nome, String mensagem) throws UserNotFoundException, CommunityNotFoundException {
        sistema.enviarMensagem(loginUsuario, nome, mensagem);
    }

    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return sistema.lerMensagem(loginUsuario);
    }

    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        return sistema.ehInimigo(sessaoId, inimigoLogin);
    }

    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        return sistema.ehPaquera(sessaoId, paqueraLogin);
    }

    public boolean ehFa(String loginFa, String idoloLogin) throws UserNotFoundException {
        return sistema.ehFa(loginFa, idoloLogin);
    }

    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelatioshipException, UserAlreadyAddedException, SelfNoteException {
        sistema.adicionarPaquera(sessaoId, paqueraLogin);
    }

    public void adicionarIdolo(String sessaoId, String idoloLogin) throws UserNotFoundException, UserAlreadyAddedException, SelfRelatioshipException {
        sistema.adicionarIdolo(sessaoId, idoloLogin);
    }

    public void adicionarInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException, SelfRelatioshipException, UserAlreadyAddedException {
        sistema.adicionarInimigo(sessaoId, inimigoLogin);
    }

    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        return sistema.getPaqueras(sessaoId);
    }

    public String getFas(String loginIdolo) throws UserNotFoundException {
        return sistema.getFas(loginIdolo);
    }

}
