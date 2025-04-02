/**
 * Classe que representa o perfil de um usu�rio no sistema Jackut.
 * Cont�m informa��es pessoais do usu�rio, lista de amigos, solicita��es pendentes e mensagens.
 *
 * @author MarcosMelo
 * @version 1.0
 */
package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.*;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String descricao;
    private String estadoCivil;
    private String aniversario;
    private String filhos;
    private String idiomas;
    private String cidadeNatal;
    private String estilo;
    private String fumo;
    private String bebo;
    private String moro;

    // Lista de amigos
    private List<String> amigos;
    private List<String> amigosPendentes;
    private Queue<String> recados;
    private List<String> recadosLidos;

    /**
     * Construtor da classe UserProfile.
     * Inicializa as listas de amigos, solicita��es pendentes e mensagens.
     */
    public UserProfile() {
        this.amigos = new ArrayList<>();
        this.amigosPendentes = new ArrayList<>();
        this.recados = new LinkedList<>();
        this.recadosLidos = new ArrayList<>();
    }

    // M�todos para acessar e editar os atributos do perfil

    /**
     * Obt�m a descri��o do perfil do usu�rio.
     *
     * @return A descri��o do perfil.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descri��o do perfil do usu�rio.
     *
     * @param descricao Nova descri��o do perfil.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obt�m o estado civil do usu�rio.
     *
     * @return O estado civil do usu�rio.
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * Define o estado civil do usu�rio.
     *
     * @param estadoCivil Novo estado civil do usu�rio.
     */
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * Obt�m a data de anivers�rio do usu�rio.
     *
     * @return A data de anivers�rio do usu�rio.
     */
    public String getAniversario() {
        return aniversario;
    }

    /**
     * Define a data de anivers�rio do usu�rio.
     *
     * @param aniversario Nova data de anivers�rio do usu�rio.
     */
    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    /**
     * Obt�m informa��es sobre os filhos do usu�rio.
     *
     * @return Informa��es sobre os filhos do usu�rio.
     */
    public String getFilhos() {
        return filhos;
    }

    /**
     * Define informa��es sobre os filhos do usu�rio.
     *
     * @param filhos Novas informa��es sobre os filhos do usu�rio.
     */
    public void setFilhos(String filhos) {
        this.filhos = filhos;
    }

    /**
     * Obt�m os idiomas falados pelo usu�rio.
     *
     * @return Os idiomas falados pelo usu�rio.
     */
    public String getIdiomas() {
        return idiomas;
    }

    /**
     * Define os idiomas falados pelo usu�rio.
     *
     * @param idiomas Novos idiomas falados pelo usu�rio.
     */
    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    /**
     * Obt�m a cidade natal do usu�rio.
     *
     * @return A cidade natal do usu�rio.
     */
    public String getCidadeNatal() {
        return cidadeNatal;
    }

    /**
     * Define a cidade natal do usu�rio.
     *
     * @param cidadeNatal Nova cidade natal do usu�rio.
     */
    public void setCidadeNatal(String cidadeNatal) {
        this.cidadeNatal = cidadeNatal;
    }

    /**
     * Obt�m o estilo musical preferido do usu�rio.
     *
     * @return O estilo musical preferido do usu�rio.
     */
    public String getEstilo() {
        return estilo;
    }

    /**
     * Define o estilo musical preferido do usu�rio.
     *
     * @param estilo Novo estilo musical preferido do usu�rio.
     */
    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    /**
     * Obt�m informa��es sobre o h�bito de fumar do usu�rio.
     *
     * @return Informa��es sobre o h�bito de fumar do usu�rio.
     */
    public String getFumo() {
        return fumo;
    }

    /**
     * Define informa��es sobre o h�bito de fumar do usu�rio.
     *
     * @param fumo Novas informa��es sobre o h�bito de fumar do usu�rio.
     */
    public void setFumo(String fumo) {
        this.fumo = fumo;
    }

    /**
     * Obt�m informa��es sobre o h�bito de beber do usu�rio.
     *
     * @return Informa��es sobre o h�bito de beber do usu�rio.
     */
    public String getBebo() {
        return bebo;
    }

    /**
     * Define informa��es sobre o h�bito de beber do usu�rio.
     *
     * @param bebo Novas informa��es sobre o h�bito de beber do usu�rio.
     */
    public void setBebo(String bebo) {
        this.bebo = bebo;
    }

    /**
     * Obt�m informa��es sobre onde o usu�rio mora.
     *
     * @return Informa��es sobre onde o usu�rio mora.
     */
    public String getMoro() {
        return moro;
    }

    /**
     * Define informa��es sobre onde o usu�rio mora.
     *
     * @param moro Novas informa��es sobre onde o usu�rio mora.
     */
    public void setMoro(String moro) {
        this.moro = moro;
    }

    // M�todos para lidar com amigos

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
    public List<String> getRecadosLidos() {
        return recadosLidos;
    }

    /**
     * Obt�m a fila de recados n�o lidos do usu�rio.
     *
     * @return Fila de recados n�o lidos.
     */
    public Queue<String> getRecados() {
        return recados;
    }

    /**
     * Verifica se um determinado atributo do perfil est� preenchido.
     *
     * @param atributo Nome do atributo a ser verificado.
     * @return true se o atributo estiver preenchido, false caso contr�rio.
     */
    public boolean isAtributoPreenchido(String atributo) {
        switch (atributo) {
            case "nome":
                return true; //n�o pode ser null (verificado na cria��o da conta)
            case "descricao":
                return descricao != null && !descricao.isEmpty();
            case "estadoCivil":
                return estadoCivil != null && !estadoCivil.isEmpty();
            case "aniversario":
                return aniversario != null && !aniversario.isEmpty();
            case "filhos":
                return filhos != null && !filhos.isEmpty();
            case "idiomas":
                return idiomas != null && !idiomas.isEmpty();
            case "cidadeNatal":
                return cidadeNatal != null && !cidadeNatal.isEmpty();
            case "estilo":
                return estilo != null && !estilo.isEmpty();
            case "fumo":
                return fumo != null && !fumo.isEmpty();
            case "bebo":
                return bebo != null && !bebo.isEmpty();
            case "moro":
                return moro != null && !moro.isEmpty();
            default:
                return false;
        }
    }
}