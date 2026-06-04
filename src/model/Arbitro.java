package model;

public class Arbitro {
    private int id;
    private String nome;
    private String nacionalidade;
    private int anosExperiencia;

    public Arbitro(int id, String nome, String nacionalidade, int anosExperiencia) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do árbitro é obrigatório.");
        }

        if (anosExperiencia < 0) {
            throw new IllegalArgumentException("Os anos de experiência não podem ser negativos.");
        }

        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.anosExperiencia = anosExperiencia;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    @Override
    public String toString() {
        return nome + " - " + nacionalidade + " (" + anosExperiencia + " anos de experiência)";
    }
}
