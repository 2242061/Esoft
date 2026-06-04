package model;

public class Deslocacao {
    private int id;
    private Equipa equipa;
    private String origem;
    private String destino;
    private String meioTransporte;
    private String data;
    private String hora;

    public Deslocacao(int id, Equipa equipa, String origem, String destino,
                      String meioTransporte, String data, String hora) {

        if (equipa == null) {
            throw new IllegalArgumentException("A equipa é obrigatória.");
        }

        if (origem == null || origem.isBlank()) {
            throw new IllegalArgumentException("A origem é obrigatória.");
        }

        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("O destino é obrigatório.");
        }

        this.id = id;
        this.equipa = equipa;
        this.origem = origem;
        this.destino = destino;
        this.meioTransporte = meioTransporte;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public Equipa getEquipa() {
        return equipa;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public String getMeioTransporte() {
        return meioTransporte;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return equipa.getNome() + " | " + origem + " -> " + destino +
                " | " + meioTransporte + " | " + data + " " + hora;
    }
}