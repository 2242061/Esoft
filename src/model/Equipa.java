package model;

public class Equipa {
    private int id;
    private String nome;
    private String pais;
    private String grupo;

    public Equipa(int id, String nome, String pais, String grupo) {
        this.id = id;
        this.nome = nome;
        this.pais = pais;
        this.grupo = grupo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPais() {
        return pais;
    }

    public String getGrupo() {
        return grupo;
    }

    @Override
    public String toString() {
        return nome + " (" + pais + ") - Grupo " + grupo;
    }
}