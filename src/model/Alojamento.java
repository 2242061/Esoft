package model;

public class Alojamento {
    private int id;
    private String nomeHotel;
    private String cidade;
    private String morada;
    private int capacidade;
    private boolean reservado;

    public Alojamento(int id, String nomeHotel, String cidade, String morada, int capacidade) {
        if (nomeHotel == null || nomeHotel.isBlank()) {
            throw new IllegalArgumentException("O nome do hotel é obrigatório.");
        }

        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser superior a zero.");
        }

        this.id = id;
        this.nomeHotel = nomeHotel;
        this.cidade = cidade;
        this.morada = morada;
        this.capacidade = capacidade;
        this.reservado = false;
    }

    public void reservar() {
        this.reservado = true;
    }

    public void cancelarReserva() {
        this.reservado = false;
    }

    public int getId() {
        return id;
    }

    public String getNomeHotel() {
        return nomeHotel;
    }

    public String getCidade() {
        return cidade;
    }

    public String getMorada() {
        return morada;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean isReservado() {
        return reservado;
    }

    @Override
    public String toString() {
        return nomeHotel + " - " + cidade + " | Capacidade: " + capacidade;
    }
}