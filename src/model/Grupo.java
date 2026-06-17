package model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private String nome;
    private List<Equipa> equipas;

    public Grupo(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do grupo é obrigatório.");
        }

        this.nome = nome;
        this.equipas = new ArrayList<>();
    }

    public void adicionarEquipa(Equipa equipa) {
        if (equipa == null) {
            throw new IllegalArgumentException("A equipa não pode ser nula.");
        }

        if (equipas.size() >= 4) {
            throw new IllegalArgumentException("Um grupo só pode ter no máximo 4 equipas.");
        }

        if (equipas.contains(equipa)) {
            throw new IllegalArgumentException("A equipa já existe neste grupo.");
        }

        equipas.add(equipa);
    }

    public String getNome() {
        return nome;
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    @Override
    public String toString() {
        return "Grupo " + nome + " - " + equipas.size() + " equipas";
    }
}