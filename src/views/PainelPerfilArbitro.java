package views;

import model.Arbitro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelPerfilArbitro {
    private JPanel painelPrincipal;
    private Arbitro arbitro;

    // Cores padronizadas da tua app
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);

    public PainelPerfilArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // TOPO: Botão Back
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.setBackground(COR_FUNDO);
        JButton btnVoltar = criarBotaoSimples("<- Back");
        btnVoltar.addActionListener(e -> {
            JPanel parent = (JPanel) painelPrincipal.getParent();
            parent.removeAll();
            parent.add(new PainelArbitros().getPainelPrincipal(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        });
        painelTopo.add(btnVoltar);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // CENTRO: Dividido em Cima (Info) e Baixo (Estatísticas)
        JPanel painelCentro = new JPanel(new BorderLayout(20, 30));
        painelCentro.setBackground(COR_FUNDO);

        // --- PARTE SUPERIOR (FOTO + INFO) ---
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        painelInfo.setBackground(COR_FUNDO);

        // "Foto" (Placeholder)
        JLabel lblFoto = new JLabel("Foto", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.GRAY);
        lblFoto.setForeground(Color.WHITE);
        lblFoto.setFont(new Font("Segoe UI", Font.BOLD, 18));
        painelInfo.add(lblFoto);

        // Informações Textuais
        JPanel painelDados = new JPanel();
        painelDados.setLayout(new BoxLayout(painelDados, BoxLayout.Y_AXIS));
        painelDados.setBackground(COR_FUNDO);

        painelDados.add(criarLabelInfo("Nome : " + arbitro.getNome()));
        painelDados.add(Box.createVerticalStrut(10));
        painelDados.add(criarLabelInfo("País : " + arbitro.getNacionalidade()));
        painelDados.add(Box.createVerticalStrut(10));
        painelDados.add(criarLabelInfo("Anos de Experiência : " + arbitro.getAnosExperiencia()));
        painelDados.add(Box.createVerticalStrut(10));
        painelDados.add(criarLabelInfo("Estatuto : Internacional FIFA"));

        painelInfo.add(painelDados);
        painelCentro.add(painelInfo, BorderLayout.NORTH);

        // --- PARTE INFERIOR (ESTATÍSTICAS) ---
        JPanel painelEstatisticas = new JPanel(new GridLayout(1, 2, 20, 0));
        painelEstatisticas.setBackground(COR_FUNDO);

        // Tabela Esquerda (Últimos Jogos)
        JPanel pnlTabela = criarCartao("Jogos Recentes");
        String[] colunas = {"Data", "Jogo", "Cartões"};
        JTable tabelaJogos = new JTable(new DefaultTableModel(colunas, 0));
        tabelaJogos.setBackground(COR_FUNDO);
        tabelaJogos.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(tabelaJogos);
        scroll.getViewport().setBackground(COR_FUNDO);
        pnlTabela.add(scroll, BorderLayout.CENTER);
        painelEstatisticas.add(pnlTabela);

        // Quadrados Direita (Sumários)
        JPanel pnlQuadrados = new JPanel(new GridLayout(2, 2, 15, 15));
        pnlQuadrados.setBackground(COR_FUNDO);

        pnlQuadrados.add(criarCartaoEstatistica("Jogos Apitados", "0 totais"));
        pnlQuadrados.add(criarCartaoEstatistica("Faltas Médias", "0 / jogo"));
        pnlQuadrados.add(criarCartaoEstatistica("Cartões Amarelos", "0 totais"));
        pnlQuadrados.add(criarCartaoEstatistica("Rating Geral", "8.5"));

        painelEstatisticas.add(pnlQuadrados);
        painelCentro.add(painelEstatisticas, BorderLayout.CENTER);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
    }

    private JLabel criarLabelInfo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return lbl;
    }

    private JPanel criarCartao(String titulo) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(COR_CARTAO);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 62, 65)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        pnl.add(lblTitulo, BorderLayout.NORTH);
        return pnl;
    }

    private JPanel criarCartaoEstatistica(String titulo, String valor) {
        JPanel pnl = criarCartao(titulo);
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setForeground(new Color(180, 180, 190));
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnl.add(lblValor, BorderLayout.CENTER);
        return pnl;
    }

    private JButton criarBotaoSimples(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}