package service;

import model.Bilhete;
import model.Jogo;

import java.util.ArrayList;
import java.util.List;

public class BilheteService {
    private List<Bilhete> bilhetes;
    private double vendasTotais;

    public BilheteService() {
        this.bilhetes = new ArrayList<>();
        this.vendasTotais = 0;
    }

    public Bilhete comprarBilhete(int id, Jogo jogo, String nomeComprador, String nif,
                                  String contacto, String seccao, int quantidade,
                                  double precoUnitario) {

        Bilhete bilhete = new Bilhete(
                id,
                jogo,
                nomeComprador,
                nif,
                contacto,
                seccao,
                quantidade,
                precoUnitario
        );

        bilhete.vender();
        bilhetes.add(bilhete);
        vendasTotais += bilhete.calcularTotal();

        return bilhete;
    }

    public int getQuantidadeBilhetesVendidos() {
        int total = 0;

        for (Bilhete bilhete : bilhetes) {
            if (bilhete.isVendido()) {
                total += bilhete.getQuantidade();
            }
        }

        return total;
    }

    public double getVendasTotais() {
        return vendasTotais;
    }

    public List<Bilhete> getBilhetes() {
        return bilhetes;
    }

    public List<Bilhete> getBilhetesPorJogo(Jogo jogo) {
        List<Bilhete> resultado = new ArrayList<>();

        for (Bilhete bilhete : bilhetes) {
            if (bilhete.getJogo().getId() == jogo.getId()) {
                resultado.add(bilhete);
            }
        }

        return resultado;
    }
}