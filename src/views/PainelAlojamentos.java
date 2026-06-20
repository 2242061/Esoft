package views;

import model.Alojamento;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class PainelAlojamentos {
    private JPanel painelPrincipal;
    private JTable tabelaAlojamentos;
    private DefaultTableModel modeloTabela;

    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);

    public PainelAlojamentos() {
        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. TOPO (Título)
        JLabel lblTitulo = new JLabel("Gestão de Alojamentos", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // 2. CENTRO (Tabela Estilizada)
        String[] colunas = {"ID", "Nome", "Cidade", "Morada", "Capacidade"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Alojamento a : CentralDeDados.getInstance().getAlojamentos()) {
            modeloTabela.addRow(new Object[]{a.getId(), a.getNomeHotel(), a.getCidade(), a.getMorada(), a.getCapacidade()});
        }

        tabelaAlojamentos = new JTable(modeloTabela);
        estilizarTabela(tabelaAlojamentos);

        JScrollPane scrollPane = new JScrollPane(tabelaAlojamentos);
        scrollPane.getViewport().setBackground(COR_CARTAO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_CARTAO));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // 3. BASE (Botões)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnAdicionar = criarBotaoPersonalizado("Adicionar Alojamento", COR_ROXO, Color.WHITE);
        btnAdicionar.addActionListener(e -> abrirDialogoAdicionar());

        painelBotoes.add(btnAdicionar);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void estilizarTabela(JTable tabela) {
        tabela.setBackground(COR_CARTAO);
        tabela.setForeground(Color.WHITE);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setRowHeight(30);
        tabela.setSelectionBackground(COR_ROXO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setShowGrid(false);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_FUNDO);
        header.setForeground(new Color(140, 140, 150));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private JButton criarBotaoPersonalizado(String texto, Color corFundo, Color corTexto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corFundo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    private void abrirDialogoAdicionar() {
        JTextField txtNome = new JTextField();
        JTextField txtCidade = new JTextField();
        JTextField txtMorada = new JTextField();
        JTextField txtCap = new JTextField();

        Object[] msg = {"Nome:", txtNome, "Cidade:", txtCidade, "Morada:", txtMorada, "Capacidade:", txtCap};
        if (JOptionPane.showConfirmDialog(painelPrincipal, msg, "Novo Alojamento", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int id = CentralDeDados.getInstance().getAlojamentos().stream().mapToInt(Alojamento::getId).max().orElse(0) + 1;
                Alojamento novo = new Alojamento(id, txtNome.getText(), txtCidade.getText(), txtMorada.getText(), Integer.parseInt(txtCap.getText()));

                CentralDeDados.getInstance().getAlojamentos().add(novo);
                modeloTabela.addRow(new Object[]{novo.getId(), novo.getNomeHotel(), novo.getCidade(), novo.getMorada(), novo.getCapacidade()});

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("alojamento.txt", true))) {
                    bw.write(id + ";" + novo.getNomeHotel() + ";" + novo.getCidade() + ";" + novo.getMorada() + ";" + novo.getCapacidade());
                    bw.newLine();
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(painelPrincipal, "Dados inválidos!"); }
        }
    }

    public JPanel getPainelPrincipal() { return painelPrincipal; }
}