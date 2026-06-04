package model;

public class Estadio {
    private int id;
    private String nome;
    private String cidade;
    private int capacidade;

    public Estadio(int id, String nome, String cidade, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public int getCapacidade() {
        return capacidade;
    }

    @Override
    public String toString() {
        return nome + " - " + cidade + " (" + capacidade + " lugares)";
    }
}