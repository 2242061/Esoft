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
    private int numeroCamisola;

    // Novo construtor simplificado (9 campos)
    // Em Jogador.java
    public Jogador(int id, String nome, String pais, String clube, String posicao,
                   double altura, double peso, int ratingGeral, int numeroCamisola) {
        this.id = id;
        this.nome = nome;
        this.pais = pais;
        this.clube = clube;
        this.posicao = posicao;
        this.altura = altura;
        this.peso = peso;
        this.ratingGeral = ratingGeral;
        this.numeroCamisola = numeroCamisola;
    }

    // Getters e Setters básicos
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getPais() { return pais; }
    public String getClube() { return clube; }
    public String getPosicao() { return posicao; }
    public double getAltura() { return altura; }
    public double getPeso() { return peso; }
    public int getRatingGeral() { return ratingGeral; }
    public int getNumeroCamisola() { return numeroCamisola; }

    public void setNome(String nome) { this.nome = nome; }
    public void setClube(String clube) { this.clube = clube; }
    public void setPosicao(String posicao) { this.posicao = posicao; }
    public void setRatingGeral(int ratingGeral) { this.ratingGeral = ratingGeral; }
    public void setNumeroCamisola(int num) { this.numeroCamisola = num; }
}