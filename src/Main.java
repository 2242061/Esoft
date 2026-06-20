import model.Alojamento;
import model.Arbitro;
import model.Bilhete;
import model.Deslocacao;
import model.Eliminatoria;
import model.Equipa;
import model.Estadio;
import model.Grupo;
import model.Jogador;
import model.Jogo;
import model.Utilizador;
import service.BilheteService;
import service.CampeonatoService;
import views.JanelaLogin;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Sistema Mundial 2026");
        frame.setContentPane(new JanelaLogin().getPainelPrincipal());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(1100, 700);
        frame.setResizable(false);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}