package service;

import model.Arbitro;
import model.Equipa;
import model.Estadio;
import model.Grupo;
import model.Jogo;

import java.util.ArrayList;
import java.util.List;

public class CampeonatoService {
    private List<Equipa> equipas;
    private List<Estadio> estadios;
    private List<Arbitro> arbitros;
    private List<Jogo> jogos;
    private List<Grupo> grupos;

    public CampeonatoService() {
        this.equipas = new ArrayList<>();
        this.estadios = new ArrayList<>();
        this.arbitros = new ArrayList<>();
        this.jogos = new ArrayList<>();
        this.grupos = new ArrayList<>();
    }

    public void adicionarEquipa(Equipa equipa) {
        if (equipa == null) {
            throw new IllegalArgumentException("A equipa não pode ser nula.");
        }

        equipas.add(equipa);
    }

    public void adicionarEstadio(Estadio estadio) {
        if (estadio == null) {
            throw new IllegalArgumentException("O estádio não pode ser nulo.");
        }

        estadios.add(estadio);
    }

    public void adicionarArbitro(Arbitro arbitro) {
        if (arbitro == null) {
            throw new IllegalArgumentException("O árbitro não pode ser nulo.");
        }

        arbitros.add(arbitro);
    }

    public void adicionarGrupo(Grupo grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("O grupo não pode ser nulo.");
        }

        grupos.add(grupo);
    }

    public Jogo criarJogo(int id, Equipa equipaCasa, Equipa equipaFora, Estadio estadio, String data, String hora) {
        Jogo jogo = new Jogo(id, equipaCasa, equipaFora, estadio, data, hora);
        jogos.add(jogo);
        return jogo;
    }

    public void atribuirArbitroAJogo(Jogo jogo, Arbitro arbitro) {
        if (jogo == null) {
            throw new IllegalArgumentException("O jogo não pode ser nulo.");
        }

        if (arbitro == null) {
            throw new IllegalArgumentException("O árbitro não pode ser nulo.");
        }

        jogo.atribuirArbitro(arbitro);
    }

    public void registarResultado(Jogo jogo, int golosCasa, int golosFora) {
        if (jogo == null) {
            throw new IllegalArgumentException("O jogo não pode ser nulo.");
        }

        jogo.registarResultado(golosCasa, golosFora);
    }

    public List<Jogo> consultarCalendario() {
        return jogos;
    }

    public List<Equipa> getEquipas() {
        return equipas;
    }

    public List<Estadio> getEstadios() {
        return estadios;
    }

    public List<Arbitro> getArbitros() {
        return arbitros;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }
}