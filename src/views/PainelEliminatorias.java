package views;

import model.Equipa;
import service.CentralDeDados;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PainelEliminatorias {
    private JPanel painelPrincipal;
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(40, 42, 54);
    private CentralDeDados centralDeDados; // Declare the variable

    public PainelEliminatorias() { // Pass the instance via constructor
        this.centralDeDados = centralDeDados; // Initialize the variable

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Eliminatorias", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel painelBracket = new JPanel(new GridLayout(0, 1, 10, 10));
        painelBracket.setBackground(COR_FUNDO);
    }


    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}