package model;

public class EstatisticaJogador {
    private Jogador jogador;
    private String adversario;
    private int golos;
    private int assistencias;
    private int cartoes;
    private double rating;
    private String data;

    public EstatisticaJogador(Jogador jogador, String adversario, int golos,
                              int assistencias, int cartoes, double rating, String data) {

        if (jogador == null) {
            throw new IllegalArgumentException("O jogador é obrigatório.");
        }

        if (golos < 0 || assistencias < 0 || cartoes < 0) {
            throw new IllegalArgumentException("As estatísticas não podem ser negativas.");
        }

        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("O rating deve estar entre 0 e 10.");
        }

        this.jogador = jogador;
        this.adversario = adversario;
        this.golos = golos;
        this.assistencias = assistencias;
        this.cartoes = cartoes;
        this.rating = rating;
        this.data = data;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public String getAdversario() {
        return adversario;
    }

    public int getGolos() {
        return golos;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public int getCartoes() {
        return cartoes;
    }

    public double getRating() {
        return rating;
    }

    public String getData() {
        return data;
    }
}