package views;

import model.Eliminatoria;
import model.Jogo;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PainelEliminatorias {
    private JPanel painelPrincipal;
    private JPanel painelBracket;
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(40, 42, 54);
    private final Color COR_AZUL = new Color(25, 118, 210);
    private boolean isAdmin;


    public PainelEliminatorias(boolean isAdmin) {
            this.isAdmin = isAdmin;

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título e Botão
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);

        JLabel lblTitulo = new JLabel("Eliminatórias", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton btnGerarElim = new JButton("GERAR SORTEIO");
        btnGerarElim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGerarElim.setBackground(COR_AZUL);
        btnGerarElim.setForeground(Color.WHITE);
        btnGerarElim.setFocusPainted(false);
        btnGerarElim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGerarElim.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        painelTopo.add(lblTitulo, BorderLayout.WEST);
        painelTopo.add(btnGerarElim, BorderLayout.EAST);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        painelBracket = new JPanel();
        painelBracket.setBackground(COR_FUNDO);
        painelPrincipal.add(painelBracket, BorderLayout.CENTER);

        btnGerarElim.addActionListener(e -> {
            CentralDeDados bd = CentralDeDados.getInstance();
            if (!bd.isFaseGruposConcluida()) {
                JOptionPane.showMessageDialog(painelPrincipal,
                        "Ainda há jogos da Fase de Grupos por realizar!",
                        "Torneio Incompleto", JOptionPane.WARNING_MESSAGE);
            } else {
                bd.gerarDezasseisAvosDeFinal();
                atualizarBracket();
            }
        });

        atualizarBracket();
    }

    private void atualizarBracket() {
        painelBracket.removeAll();
        List<Eliminatoria> jogosGerados = CentralDeDados.getInstance().getListaEliminatorias();

        if (jogosGerados.isEmpty()) {
            painelBracket.setLayout(new BorderLayout());
            JLabel lblVazio = new JLabel("O Sorteio ainda não foi gerado. Conclua os grupos primeiro.", SwingConstants.CENTER);
            lblVazio.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblVazio.setForeground(new Color(100, 100, 120));
            painelBracket.add(lblVazio, BorderLayout.CENTER);
        } else {
            // Desenho do Torneio: Coluna Esquerda | Centro (Troféu) | Coluna Direita
            painelBracket.setLayout(new BorderLayout(40, 0));

            JPanel colunaEsq = new JPanel(new GridLayout(4, 1, 0, 15));
            JPanel colunaDir = new JPanel(new GridLayout(4, 1, 0, 15));
            colunaEsq.setBackground(COR_FUNDO);
            colunaDir.setBackground(COR_FUNDO);

            // Preencher os 8 jogos (4 de cada lado)
            for (int i = 0; i < jogosGerados.size(); i++) {
                Jogo j = jogosGerados.get(i).getJogo();
                JPanel cartao = criarCartaoPremium(j.getEquipaCasa().getNome(), j.getEquipaFora().getNome(), j.getData() + " às " + j.getHora());

                if (i < 4) colunaEsq.add(cartao);
                else colunaDir.add(cartao);
            }

            JLabel lblCentro = new JLabel("🏆", SwingConstants.CENTER);
            lblCentro.setFont(new Font("Segoe UI", Font.PLAIN, 80));

            painelBracket.add(colunaEsq, BorderLayout.WEST);
            painelBracket.add(colunaDir, BorderLayout.EAST);
            painelBracket.add(lblCentro, BorderLayout.CENTER);
        }

        painelBracket.revalidate();
        painelBracket.repaint();
    }

    // Design Premium do Cartão
    private JPanel criarCartaoPremium(String equipa1, String equipa2, String detalhes) {
        JPanel cartao = new JPanel(new BorderLayout());
        cartao.setBackground(COR_CARTAO);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 63, 80), 2), // Borda subtil
                new EmptyBorder(10, 15, 10, 15)
        ));

        // Data no topo
        JLabel lblData = new JLabel(detalhes, SwingConstants.CENTER);
        lblData.setForeground(new Color(150, 150, 170));
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cartao.add(lblData, BorderLayout.NORTH);

        // Nomes das equipas
        JPanel pnlEquipas = new JPanel(new GridLayout(1, 3));
        pnlEquipas.setBackground(COR_CARTAO);
        pnlEquipas.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel l1 = new JLabel(equipa1, SwingConstants.RIGHT);
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lVs = new JLabel("VS", SwingConstants.CENTER);
        lVs.setForeground(COR_AZUL);
        lVs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel l2 = new JLabel(equipa2, SwingConstants.LEFT);
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 15));

        pnlEquipas.add(l1);
        pnlEquipas.add(lVs);
        pnlEquipas.add(l2);

        cartao.add(pnlEquipas, BorderLayout.CENTER);
        return cartao;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}