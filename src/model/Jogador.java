package model;

public class Jogador {
    private int id;
    private String nome;
    private String pais;
    private String clube;
    private String posicao;
    private double altura;
    private double peso;
    private int ratingGeral;

    public Jogador(int id, String nome, String pais, String clube, String posicao,
                   double altura, double peso, int ratingGeral) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do jogador é obrigatório.");
        }

        if (ratingGeral < 0 || ratingGeral > 100) {
            throw new IllegalArgumentException("O rating deve estar entre 0 e 100.");
        }

        this.id = id;
        this.nome = nome;
        this.pais = pais;
        this.clube = clube;
        this.posicao = posicao;
        this.altura = altura;
        this.peso = peso;
        this.ratingGeral = ratingGeral;
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

    public String getClube() {
        return clube;
    }

    public String getPosicao() {
        return posicao;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public int getRatingGeral() {
        return ratingGeral;
    }

    @Override
    public String toString() {
        return nome + " | " + pais + " | " + clube + " | " + posicao + " | Rating: " + ratingGeral;
    }
}