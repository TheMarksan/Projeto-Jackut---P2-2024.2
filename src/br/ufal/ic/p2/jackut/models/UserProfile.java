/**
 * Classe que representa o perfil de um usuário no sistema Jackut.
 * Contém informações pessoais do usuário, lista de amigos, solicitações pendentes e mensagens.
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
     * Inicializa as listas de amigos, solicitações pendentes e mensagens.
     */
    public UserProfile() {
        this.amigos = new ArrayList<>();
        this.amigosPendentes = new ArrayList<>();
        this.recados = new LinkedList<>();
        this.recadosLidos = new ArrayList<>();
    }

    // Métodos para acessar e editar os atributos do perfil

    /**
     * Obtém a descrição do perfil do usuário.
     *
     * @return A descrição do perfil.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do perfil do usuário.
     *
     * @param descricao Nova descrição do perfil.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o estado civil do usuário.
     *
     * @return O estado civil do usuário.
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * Define o estado civil do usuário.
     *
     * @param estadoCivil Novo estado civil do usuário.
     */
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * Obtém a data de aniversário do usuário.
     *
     * @return A data de aniversário do usuário.
     */
    public String getAniversario() {
        return aniversario;
    }

    /**
     * Define a data de aniversário do usuário.
     *
     * @param aniversario Nova data de aniversário do usuário.
     */
    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    /**
     * Obtém informações sobre os filhos do usuário.
     *
     * @return Informações sobre os filhos do usuário.
     */
    public String getFilhos() {
        return filhos;
    }

    /**
     * Define informações sobre os filhos do usuário.
     *
     * @param filhos Novas informações sobre os filhos do usuário.
     */
    public void setFilhos(String filhos) {
        this.filhos = filhos;
    }

    /**
     * Obtém os idiomas falados pelo usuário.
     *
     * @return Os idiomas falados pelo usuário.
     */
    public String getIdiomas() {
        return idiomas;
    }

    /**
     * Define os idiomas falados pelo usuário.
     *
     * @param idiomas Novos idiomas falados pelo usuário.
     */
    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    /**
     * Obtém a cidade natal do usuário.
     *
     * @return A cidade natal do usuário.
     */
    public String getCidadeNatal() {
        return cidadeNatal;
    }

    /**
     * Define a cidade natal do usuário.
     *
     * @param cidadeNatal Nova cidade natal do usuário.
     */
    public void setCidadeNatal(String cidadeNatal) {
        this.cidadeNatal = cidadeNatal;
    }

    /**
     * Obtém o estilo musical preferido do usuário.
     *
     * @return O estilo musical preferido do usuário.
     */
    public String getEstilo() {
        return estilo;
    }

    /**
     * Define o estilo musical preferido do usuário.
     *
     * @param estilo Novo estilo musical preferido do usuário.
     */
    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    /**
     * Obtém informações sobre o hábito de fumar do usuário.
     *
     * @return Informações sobre o hábito de fumar do usuário.
     */
    public String getFumo() {
        return fumo;
    }

    /**
     * Define informações sobre o hábito de fumar do usuário.
     *
     * @param fumo Novas informações sobre o hábito de fumar do usuário.
     */
    public void setFumo(String fumo) {
        this.fumo = fumo;
    }

    /**
     * Obtém informações sobre o hábito de beber do usuário.
     *
     * @return Informações sobre o hábito de beber do usuário.
     */
    public String getBebo() {
        return bebo;
    }

    /**
     * Define informações sobre o hábito de beber do usuário.
     *
     * @param bebo Novas informações sobre o hábito de beber do usuário.
     */
    public void setBebo(String bebo) {
        this.bebo = bebo;
    }

    /**
     * Obtém informações sobre onde o usuário mora.
     *
     * @return Informações sobre onde o usuário mora.
     */
    public String getMoro() {
        return moro;
    }

    /**
     * Define informações sobre onde o usuário mora.
     *
     * @param moro Novas informações sobre onde o usuário mora.
     */
    public void setMoro(String moro) {
        this.moro = moro;
    }

    // Métodos para lidar com amigos

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
    public List<String> getRecadosLidos() {
        return recadosLidos;
    }

    /**
     * Obtém a fila de recados não lidos do usuário.
     *
     * @return Fila de recados não lidos.
     */
    public Queue<String> getRecados() {
        return recados;
    }

    /**
     * Verifica se um determinado atributo do perfil está preenchido.
     *
     * @param atributo Nome do atributo a ser verificado.
     * @return true se o atributo estiver preenchido, false caso contrário.
     */
    public boolean isAtributoPreenchido(String atributo) {
        switch (atributo) {
            case "nome":
                return true; //não pode ser null (verificado na criação da conta)
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