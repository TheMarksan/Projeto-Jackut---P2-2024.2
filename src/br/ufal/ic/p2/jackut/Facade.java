package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.Friendship.*;
import br.ufal.ic.p2.jackut.exceptions.Message.*;
import br.ufal.ic.p2.jackut.exceptions.Profile.*;
import br.ufal.ic.p2.jackut.exceptions.Session.*;
import br.ufal.ic.p2.jackut.exceptions.User.*;

/**
 * Fachada principal do sistema Jackut que fornece a interface p�blica para intera��o com o sistema.
 * Atua como um ponto �nico de acesso para todas as opera��es do sistema.
 *
 * @author MarcosMelo
 * @version 1.0
 */
public class Facade {
    private final Sistema sistema;

    /**
     * Construtor que inicializa a fachada criando uma nova inst�ncia do sistema.
     */
    public Facade() {
        this.sistema = new Sistema();
    }

    /**
     * Reinicia o sistema removendo todos os usu�rios e sess�es.
     */
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    /**
     * Obt�m o valor de um atributo espec�fico do usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Atributo a ser consultado
     * @return Valor do atributo solicitado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws AttributeNotSetException Se o atributo n�o estiver definido
     * @throws InvalidAttributeException Se o atributo n�o existir
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UserNotFoundException, AttributeNotSetException, InvalidAttributeException {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param nome Nome completo do usu�rio
     * @param senha Senha de acesso
     * @param login Identificador �nico
     * @throws UserCreationException Se os dados forem inv�lidos ou o login j� existir
     */
    public void criarUsuario(String nome, String senha, String login) throws UserCreationException {
        sistema.criarUsuario(nome, senha, login);
    }

    /**
     * Autentica um usu�rio e inicia uma nova sess�o.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return Login da sess�o criada
     * @throws SessionOpeningException Se a autentica��o falhar
     */
    public String abrirSessao(String login, String senha) throws SessionOpeningException {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Encerra o sistema, salvando os dados atuais.
     */
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    /**
     * Edita um atributo do perfil do usu�rio.
     *
     * @param id Login do usu�rio
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor do atributo
     * @throws InvalidAttributeException Se o atributo n�o existir
     * @throws ProfileEditException Se ocorrer erro na edi��o do perfil
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
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
     * Adiciona um amigo para o usu�rio.
     *
     * @param loginUsuario Login do usu�rio que est� adicionando
     * @param loginAmigo Login do amigo a ser adicionado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se j� forem amigos ou houver solicita��o pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        sistema.adicionarAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Remove um amigo da lista de amigos do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @param loginAmigo Login do amigo a ser removido
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws FriendshipException Se n�o forem amigos
     */
    public void removerAmigo(String loginUsuario, String loginAmigo)
            throws UserNotFoundException, FriendshipException {
        sistema.removerAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param loginUsuario Login do primeiro usu�rio
     * @param loginAmigo Login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigo(String loginUsuario, String loginAmigo) throws UserNotFoundException {
        return sistema.ehAmigo(loginUsuario, loginAmigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio formatada.
     *
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getAmigos(String login) throws UserNotFoundException {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param loginUsuario Login do remetente
     * @param loginRecado Login do destinat�rio
     * @param recado Conte�do do recado
     * @throws UserNotFoundException Se algum usu�rio n�o for encontrado
     * @throws SelfMessageException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfMessageException {
        sistema.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Conte�do do recado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens para ler
     */
    public String lerRecado(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return sistema.lerRecado(loginUsuario);
    }
}
