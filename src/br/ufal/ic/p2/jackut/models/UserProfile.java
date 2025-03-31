package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public UserProfile() {
        this.amigos = new ArrayList<>();
        this.amigosPendentes = new ArrayList<>();
    }

    // Métodos para acessar e editar os atributos do perfil

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getAniversario() {
        return aniversario;
    }

    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    public String getFilhos() {
        return filhos;
    }

    public void setFilhos(String filhos) {
        this.filhos = filhos;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public String getCidadeNatal() {
        return cidadeNatal;
    }

    public void setCidadeNatal(String cidadeNatal) {
        this.cidadeNatal = cidadeNatal;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getFumo() {
        return fumo;
    }

    public void setFumo(String fumo) {
        this.fumo = fumo;
    }

    public String getBebo() {
        return bebo;
    }

    public void setBebo(String bebo) {
        this.bebo = bebo;
    }

    public String getMoro() {
        return moro;
    }

    public void setMoro(String moro) {
        this.moro = moro;
    }

    // Métodos para lidar com amigos

    public List<String> getAmigos() {
        return amigos;
    }

    public List<String> getAmigosPendentes() {
        return amigosPendentes;
    }


    public void adicionarAmigoPendente(String loginAmigo) {
        if (!amigosPendentes.contains(loginAmigo)) {
            amigosPendentes.add(loginAmigo);
        }
    }

    public void removerAmigoPendente(String loginAmigo) {
        amigosPendentes.remove(loginAmigo);
    }

    // Verificação se o atributo está preenchido
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
