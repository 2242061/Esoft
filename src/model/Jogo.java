package model;

public class Jogo {
    private int id;
    private Equipa equipaCasa;
    private Equipa equipaFora;
    private Estadio estadio;
    private String data;
    private String hora;

    public Jogo(int id, Equipa equipaCasa, Equipa equipaFora, Estadio estadio, String data, String hora) {
        if (equipaCasa == null || equipaFora == null) {
            throw new IllegalArgumentException("As equipas não podem ser nulas.");
        }

        if (equipaCasa.getId() == equipaFora.getId()) {
            throw new IllegalArgumentException("Uma equipa não pode jogar contra si própria.");
        }

        if (estadio == null) {
            throw new IllegalArgumentException("O estádio não pode ser nulo.");
        }

        this.id = id;
        this.equipaCasa = equipaCasa;
        this.equipaFora = equipaFora;
        this.estadio = estadio;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public Equipa getEquipaCasa() {
        return equipaCasa;
    }

    public Equipa getEquipaFora() {
        return equipaFora;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getInfoJogo() {
        return equipaCasa.getNome() + " vs " + equipaFora.getNome()
                + " | " + estadio.getNome()
                + " | " + data + " às " + hora;
    }
}