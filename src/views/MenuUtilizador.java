package views;

import javax.swing.*;
import java.awt.*;

public class MenuUtilizador {
    private JPanel painelPrincipal;
    private JPanel painelMenuLateral;
    private JPanel painelConteudo;

    private JButton btnGrupos;
    private JButton btnEliminatorias;
    private JButton btnCalendario;

    public MenuUtilizador() {
        // 1. INICIALIZAR OS COMPONENTES (É isto que resolve o NullPointerException!)
        painelPrincipal = new JPanel(new BorderLayout());
        painelMenuLateral = new JPanel();
        painelConteudo = new JPanel(new BorderLayout());

        btnGrupos = new JButton("Grupos");
        btnEliminatorias = new JButton("Eliminatórias");
        btnCalendario = new JButton("Calendário");

        // 2. CONFIGURAR O MENU LATERAL
        painelMenuLateral.setLayout(new BoxLayout(painelMenuLateral, BoxLayout.Y_AXIS));
        painelMenuLateral.setBackground(new Color(18, 19, 25)); // Cor escura
        painelMenuLateral.setPreferredSize(new Dimension(200, 0));

        estilizarBotao(btnGrupos);
        estilizarBotao(btnEliminatorias);
        estilizarBotao(btnCalendario);

        // Adicionar os botões com algum espaçamento
        painelMenuLateral.add(Box.createVerticalStrut(50));
        painelMenuLateral.add(btnGrupos);
        painelMenuLateral.add(Box.createVerticalStrut(15));
        painelMenuLateral.add(btnEliminatorias);
        painelMenuLateral.add(Box.createVerticalStrut(15));
        painelMenuLateral.add(btnCalendario);

        // 3. JUNTAR TUDO NO PAINEL PRINCIPAL
        painelPrincipal.add(painelMenuLateral, BorderLayout.WEST);
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        // ==========================================
        // 4. AÇÕES DOS BOTÕES (Apenas chamam as vistas)
        // ==========================================

        btnGrupos.addActionListener(e -> {
            mudarPainel(new PainelGrupos().getPainelPrincipal());
        });

        btnEliminatorias.addActionListener(e -> {
            // Lembra-te que passámos a variável "isAdmin = false" no painel de eliminatórias
            // Isto faz com que o botão de "Gerar Sorteio" fique invisível para o USER!
            mudarPainel(new PainelEliminatorias(false).getPainelPrincipal());
        });

        btnCalendario.addActionListener(e -> {

            mudarPainel(new PainelCalendario().getPainelPrincipal());

        });

        // 5. CARREGAR O PRIMEIRO PAINEL POR DEFEITO
        mudarPainel(new PainelCalendario().getPainelPrincipal());
    }

    // Método mágico que troca a view do lado direito
    private void mudarPainel(JPanel novoPainel) {
        painelConteudo.removeAll();
        painelConteudo.add(novoPainel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // Método simples para deixar os botões com bom aspeto
    private void estilizarBotao(JButton btn) {
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}