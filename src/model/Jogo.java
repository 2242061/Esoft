package model;

public class Jogo {
    private int id;
    private Equipa equipaCasa;
    private Equipa equipaFora;
    private Estadio estadio;
    private Arbitro arbitro;
    private String data;
    private String hora;
    private int golosCasa;
    private int golosFora;
    private boolean terminado;

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
        this.golosCasa = 0;
        this.golosFora = 0;
        this.terminado = false;
    }

    public void atribuirArbitro(Arbitro arbitro) {
        if (arbitro == null) {
            throw new IllegalArgumentException("O árbitro não pode ser nulo.");
        }

        this.arbitro = arbitro;
    }

    public void registarResultado(int golosCasa, int golosFora) {
        if (golosCasa < 0 || golosFora < 0) {
            throw new IllegalArgumentException("Os golos não podem ser negativos.");
        }

        this.golosCasa = golosCasa;
        this.golosFora = golosFora;
        this.terminado = true;
    }

    public Equipa obterVencedor() {
        if (!terminado) {
            return null;
        }

        if (golosCasa > golosFora) {
            return equipaCasa;
        }

        if (golosFora > golosCasa) {
            return equipaFora;
        }

        return null;
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

    public Arbitro getArbitro() {
        return arbitro;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public int getGolosCasa() {
        return golosCasa;
    }

    public int getGolosFora() {
        return golosFora;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public String getInfoJogo() {
        return equipaCasa.getNome() + " vs " + equipaFora.getNome()
                + " | " + estadio.getNome()
                + " | " + data + " às " + hora;
    }

    @Override
    public String toString() {
        if (terminado) {
            return getInfoJogo() + " | Resultado: " + golosCasa + " - " + golosFora;
        }

        return getInfoJogo() + " | Por realizar";
    }
}