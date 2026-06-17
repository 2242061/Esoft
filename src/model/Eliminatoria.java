package model;

public class Eliminatoria {
    private int id;
    private String fase;
    private Jogo jogo;
    private Equipa vencedor;

    public Eliminatoria(int id, String fase, Jogo jogo) {
        if (fase == null || fase.isBlank()) {
            throw new IllegalArgumentException("A fase da eliminatória é obrigatória.");
        }

        if (jogo == null) {
            throw new IllegalArgumentException("O jogo é obrigatório.");
        }

        this.id = id;
        this.fase = fase;
        this.jogo = jogo;
        this.vencedor = null;
    }

    public void definirVencedor(Equipa vencedor) {
        if (vencedor == null) {
            throw new IllegalArgumentException("O vencedor não pode ser nulo.");
        }

        if (vencedor.getId() != jogo.getEquipaCasa().getId()
                && vencedor.getId() != jogo.getEquipaFora().getId()) {
            throw new IllegalArgumentException("O vencedor tem de ser uma das equipas do jogo.");
        }

        this.vencedor = vencedor;
    }

    public int getId() {
        return id;
    }

    public String getFase() {
        return fase;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public Equipa getVencedor() {
        return vencedor;
    }

    @Override
    public String toString() {
        String textoVencedor = vencedor == null ? "Ainda não definido" : vencedor.getNome();

        return fase + " | " + jogo.getInfoJogo() + " | Vencedor: " + textoVencedor;
    }
}