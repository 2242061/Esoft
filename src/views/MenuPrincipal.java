package views;

import service.CentralDeDados;
import model.Estadio;
import model.Jogador;
import model.Jogo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal {
    private JPanel painelPrincipal;
    private JPanel painelMenuLateral;
    private JPanel painelConteudo;

    private JLabel Home;

    private JButton gruposButton;
    private JButton alojamentoButton;
    private JButton calendarioButton;
    private JButton bilhetesButton;
    private JButton estadiosButton;
    private JButton arbitrosButton;
    private JButton estatisticasButton;
    private JButton eliminatoriaButton;


    public MenuPrincipal() {
        // 1. Ir buscar a nossa Base de Dados central
        CentralDeDados bd = CentralDeDados.getInstance();

        Dimension tamanhoMenu = new Dimension(190, 700);
        painelMenuLateral.setPreferredSize(tamanhoMenu);
        painelMenuLateral.setMinimumSize(tamanhoMenu);
        painelMenuLateral.setMaximumSize(tamanhoMenu);

        estilizarBotaoMenu(estatisticasButton);
        estilizarBotaoMenu(gruposButton);
        estilizarBotaoMenu(alojamentoButton);
        estilizarBotaoMenu(calendarioButton);
        estilizarBotaoMenu(bilhetesButton);
        estilizarBotaoMenu(estadiosButton);
        estilizarBotaoMenu(arbitrosButton);
        estilizarBotaoMenu(eliminatoriaButton);

        painelConteudo.add(getDashboardPanel(), BorderLayout.CENTER);

         // Ligar o botão GRUPOS
        gruposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Grupos
                painelConteudo.removeAll();
                PainelGrupos paginaGrupos = new PainelGrupos();
                painelConteudo.add(paginaGrupos.getPainelPrincipal(), BorderLayout.CENTER);
                painelConteudo.revalidate();
                painelConteudo.repaint();
            }
        });

        //ESTATISTICAS
        estatisticasButton.addActionListener(e -> {
            painelConteudo.removeAll();
            painelConteudo.add(getDashboardPanel(), BorderLayout.CENTER);

            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        //ESTADIOS
        estadiosButton.addActionListener(e -> {
            painelConteudo.removeAll();
            painelConteudo.add(new PainelEstadios().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });

        //CALENDÁRIO
        calendarioButton.addActionListener(e -> {
            painelConteudo.removeAll();
            painelConteudo.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        //ARBITROS
        arbitrosButton.addActionListener(e -> {

            painelConteudo.removeAll();
            painelConteudo.add(new PainelArbitros().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        //ALOJAMENTO
        alojamentoButton.addActionListener(e -> {

            painelConteudo.removeAll();
            painelConteudo.add(new PainelAlojamentos().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        //ELIMINATORIAS
        eliminatoriaButton.addActionListener(e -> {

            painelConteudo.removeAll();
            painelConteudo.add(new PainelEliminatorias().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
    }

    private JPanel getDashboardPanel() {
        // Painel principal do Dashboard (Container geral)
        JPanel painelDashboard = new JPanel(new BorderLayout(20, 20));
        painelDashboard.setBackground(new Color(18, 19, 25));
        painelDashboard.setBorder(new EmptyBorder(20, 20, 20, 20));

        // A. Linha de Cima: 2 Cards Grandes
        JPanel linhaCima = new JPanel(new GridLayout(1, 2, 20, 0));
        linhaCima.setOpaque(false);
        linhaCima.add(criarCardGrande("JOGO COM MAIOR PROFIT", "Portugal vs Espanha\n€ 150.000"));
        linhaCima.add(criarCardGrande("VENDAS TOTAIS", "Total: € 2.500.000\nBilhetes vendidos: 45.000"));

        // B. Linha de Baixo: 3 Cards Pequenos
        JPanel linhaBaixo = new JPanel(new GridLayout(1, 3, 20, 0));
        linhaBaixo.setOpaque(false);
        CentralDeDados bd = CentralDeDados.getInstance();

        Jogo j = bd.getProximoJogo();
        Jogador m = bd.getMelhorMarcador();
        Estadio e = bd.getMaiorEstadio();

        linhaBaixo.add(criarCardPequeno("PRÓXIMO JOGO",
                "<html><center>" + (j != null ? j.getEquipaCasa().getNome() + " vs " + j.getEquipaFora().getNome()+ "<br>"+j.getData()+" - "+j.getHora(): "Sem jogos") + "</center></html>"));

        linhaBaixo.add(criarCardPequeno("MELHOR MARCADOR",
                "<html><center>" + (m != null ? m.getNumeroCamisola()+" - "+m.getNome() + "("+ m.getPais()+ ")<br>Golos: " + bd.getTotalGolos(m.getId()) : "Sem dados") + "</center></html>"));

        linhaBaixo.add(criarCardPequeno("MAIOR ESTÁDIO",
                "<html><center>" + (e != null ? e.getNome() +"<br>"+ e.getCidade()+ "<br>Capacidade: " + e.getCapacidade() : "Sem dados") + "</center></html>")); // Adiciona as linhas ao painel principal
        painelDashboard.add(linhaCima, BorderLayout.CENTER);
        painelDashboard.add(linhaBaixo, BorderLayout.SOUTH);

        return painelDashboard;
    }

    private JPanel criarCardGrande(String titulo, String info) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(28, 29, 38)); // Cor de fundo mais escura
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setForeground(new Color(114, 72, 232)); // Cor Roxo
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel i = new JLabel("<html><center>" + info.replace("\n", "<br>") + "</center></html>", SwingConstants.CENTER);
        i.setForeground(Color.WHITE);
        i.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        card.add(t, BorderLayout.NORTH);
        card.add(i, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarCardPequeno(String titulo, String info) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE); // Manter branco para contraste
        card.setPreferredSize(new Dimension(200, 120));

        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel i = new JLabel(info, SwingConstants.CENTER);
        i.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        card.add(t, BorderLayout.NORTH);
        card.add(i, BorderLayout.CENTER);
        return card;
    }private void estilizarBotaoMenu(JButton btn) {
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);

        // AS 2 LINHAS MÁGICAS PARA PARAR O "TREMOR" E AUMENTO:
        btn.setRolloverEnabled(false); // Desliga os efeitos nativos do Windows/Mac
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Fixa o tamanho da margem para sempre

        // Cores base do botão
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(114, 72, 232));
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
        });
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}