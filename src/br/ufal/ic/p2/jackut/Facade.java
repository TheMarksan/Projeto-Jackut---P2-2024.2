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
        this.sistema = Sistema.getInstance();
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
     * @throws InvalidLoginException Se o login for inv�lido
     * @throws InvalidPasswordException Se a senha for inv�lida
     * @throws AccountAlreadyExistsException Se o login j� existir
     */
    public void criarUsuario(String nome, String senha, String login) throws InvalidLoginException, InvalidPasswordException, AccountAlreadyExistsException {
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
            throws UserNotFoundException, FriendshipException, SelfRelationshipException, UserAlreadyAddedException {
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
     * @throws SelfNoteException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String loginUsuario, String loginRecado, String recado)
            throws UserNotFoundException, SelfNoteException {
        sistema.enviarRecado(loginUsuario, loginRecado, recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio.
     *
     * @param loginUsuario Login do usu�rio
     * @return Conte�do do recado
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyNotesException Se n�o houver mensagens para ler
     */
    public Note lerRecado(String loginUsuario) throws UserNotFoundException, EmptyNotesException {
        return sistema.lerRecado(loginUsuario);
    }

    /**
     * Cria uma nova comunidade com o nome e descri��o fornecidos, pertencente ao usu�rio especificado.
     *
     * @param loginUsuario Login do usu�rio que est� criando a comunidade
     * @param nome Nome da comunidade a ser criada
     * @param descricao Descri��o da comunidade
     * @throws CommunityCreationException Se ocorrer um erro ao criar a comunidade
     * @throws UserNotFoundException Se o usu�rio especificado n�o existir
     */
    public void criarComunidade(String loginUsuario, String nome, String descricao) throws CommunityCreationException, UserNotFoundException {
        sistema.criarComunidade(loginUsuario, nome, descricao);
    }

    /**
     * Obt�m a descri��o de uma comunidade existente.
     *
     * @param nome Nome da comunidade
     * @return Descri��o da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o for encontrada
     */
    public String getDescricaoComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getDescricaoComunidade(nome);
    }

    /**
     * Obt�m o dono (criador) de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o for encontrada
     */
    public String getDonoComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getDonoComunidade(nome);
    }

    /**
     * Obt�m a lista de membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Lista de membros da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o for encontrada
     */
    public String getMembrosComunidade(String nome) throws CommunityNotFoundException {
        return sistema.getMembrosComunidade(nome);
    }

    /**
     * Adiciona um usu�rio a uma comunidade existente.
     *
     * @param loginUsuario Login do usu�rio a ser adicionado
     * @param nome Nome da comunidade
     * @throws CommunityNotFoundException Se a comunidade n�o for encontrada
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void adicionarComunidade(String loginUsuario, String nome) throws CommunityNotFoundException, UserNotFoundException {
        sistema.adicionarComunidade(loginUsuario, nome);
    }

    /**
     * Obt�m a lista de comunidades �s quais um usu�rio pertence.
     *
     * @param loginUsuario Login do usu�rio
     * @return Lista de comunidades do usu�rio
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getComunidades(String loginUsuario) throws UserNotFoundException {
        return sistema.getComunidades(loginUsuario);
    }

    /**
     * Envia uma mensagem para uma comunidade.
     *
     * @param loginUsuario Login do usu�rio que est� enviando a mensagem
     * @param nome Nome da comunidade destinat�ria
     * @param mensagem Conte�do da mensagem
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws CommunityNotFoundException Se a comunidade n�o for encontrada
     */
    public void enviarMensagem(String loginUsuario, String nome, String mensagem) throws UserNotFoundException, CommunityNotFoundException {
        sistema.enviarMensagem(loginUsuario, nome, mensagem);
    }

    /**
     * L� a pr�xima mensagem destinada ao usu�rio.
     *
     * @param loginUsuario Login do usu�rio que est� lendo a mensagem
     * @return Objeto Message contendo a mensagem
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     * @throws EmptyMessagesException Se n�o houver mensagens para ler
     */
    public Message lerMensagem(String loginUsuario) throws UserNotFoundException, EmptyMessagesException {
        return sistema.lerMensagem(loginUsuario);
    }

    /**
     * Verifica se um usu�rio � inimigo de outro.
     *
     * @param sessaoId Login do usu�rio que est� verificando
     * @param inimigoLogin Login do poss�vel inimigo
     * @return true se for inimigo, false caso contr�rio
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     */
    public boolean ehInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException {
        return sistema.ehInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Verifica se um usu�rio tem outro como paquera.
     *
     * @param sessaoId Login do usu�rio que est� verificando
     * @param paqueraLogin Login da poss�vel paquera
     * @return true se for paquera, false caso contr�rio
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     */
    public boolean ehPaquera(String sessaoId, String paqueraLogin) throws UserNotFoundException {
        return sistema.ehPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Verifica se um usu�rio � f� de outro.
     *
     * @param loginFa Login do poss�vel f�
     * @param idoloLogin Login do poss�vel �dolo
     * @return true se for f�, false caso contr�rio
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     */
    public boolean ehFa(String loginFa, String idoloLogin) throws UserNotFoundException {
        return sistema.ehFa(loginFa, idoloLogin);
    }

    /**
     * Adiciona um usu�rio como paquera de outro.
     *
     * @param sessaoId Login do usu�rio que est� adicionando a paquera
     * @param paqueraLogin Login da paquera a ser adicionada
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se a paquera j� foi adicionada anteriormente
     * @throws SelfNoteException Se tentar adicionar uma rela��o consigo mesmo
     */
    public void adicionarPaquera(String sessaoId, String paqueraLogin)
            throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException, SelfNoteException {
        sistema.adicionarPaquera(sessaoId, paqueraLogin);
    }

    /**
     * Adiciona um usu�rio como �dolo de outro.
     *
     * @param sessaoId Login do usu�rio que est� adicionando o �dolo
     * @param idoloLogin Login do �dolo a ser adicionado
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     * @throws UserAlreadyAddedException Se o �dolo j� foi adicionado anteriormente
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     */
    public void adicionarIdolo(String sessaoId, String idoloLogin) throws UserNotFoundException, UserAlreadyAddedException, SelfRelationshipException {
        sistema.adicionarIdolo(sessaoId, idoloLogin);
    }

    /**
     * Adiciona um usu�rio como inimigo de outro.
     *
     * @param sessaoId Login do usu�rio que est� adicionando o inimigo
     * @param inimigoLogin Login do inimigo a ser adicionado
     * @throws UserNotFoundException Se algum dos usu�rios n�o for encontrado
     * @throws SelfRelationshipException Se tentar adicionar a si mesmo
     * @throws UserAlreadyAddedException Se o inimigo j� foi adicionado anteriormente
     */
    public void adicionarInimigo(String sessaoId, String inimigoLogin) throws UserNotFoundException, SelfRelationshipException, UserAlreadyAddedException {
        sistema.adicionarInimigo(sessaoId, inimigoLogin);
    }

    /**
     * Obt�m a lista de paqueras de um usu�rio.
     *
     * @param sessaoId Login do usu�rio
     * @return Lista de paqueras do usu�rio
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getPaqueras(String sessaoId) throws UserNotFoundException {
        return sistema.getPaqueras(sessaoId);
    }

    /**
     * Obt�m a lista de f�s de um usu�rio.
     *
     * @param loginIdolo Login do �dolo
     * @return Lista de f�s do usu�rio
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public String getFas(String loginIdolo) throws UserNotFoundException {
        return sistema.getFas(loginIdolo);
    }

    /**
     * Remove um usu�rio do sistema.
     *
     * @param sessaoId Login do usu�rio a ser removido
     * @throws UserNotFoundException Se o usu�rio n�o for encontrado
     */
    public void removerUsuario(String sessaoId) throws UserNotFoundException {
        sistema.removerUsuario(sessaoId);
    }

}
