package views;

import model.Estadio;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PainelEstadios {
    private JPanel painelPrincipal;
    private DefaultTableModel modelo; // Tornar o modelo global para o atualizar facilmente

    // Paleta de Cores Premium
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);

    public PainelEstadios() {
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);

        // ==========================================
        // 1. TOPO: Título e Botão
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);
        painelTopo.setBorder(new EmptyBorder(30, 40, 20, 40));

        JLabel lblTitulo = new JLabel("ESTÁDIOS DO MUNDIAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        painelTopo.add(lblTitulo, BorderLayout.WEST);

        // NOVO: Botão Adicionar
        JButton btnAdicionar = criarBotaoRedondo("+ Adicionar Estádio", COR_ROXO, Color.WHITE);
        btnAdicionar.addActionListener(e -> abrirDialogoNovoEstadio());

        JPanel pnlBotoesTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBotoesTopo.setOpaque(false);
        pnlBotoesTopo.add(btnAdicionar);
        painelTopo.add(pnlBotoesTopo, BorderLayout.EAST);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTRO: Tabela em Cartão Arredondado
        // ==========================================
        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(COR_FUNDO);
        painelCentro.setBorder(new EmptyBorder(0, 40, 40, 40));

        RoundedPanel cartaoTabela = new RoundedPanel(20, COR_CARTAO);
        cartaoTabela.setLayout(new BorderLayout());
        cartaoTabela.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Dados da Tabela
        String[] colunas = {"ID", "Nome do Estádio", "Cidade", "Capacidade"};

        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição
            }
        };

        for (Estadio e : CentralDeDados.getInstance().getEstadios()) {
            modelo.addRow(new Object[]{e.getId(), e.getNome(), e.getCidade(), String.format("%,d", e.getCapacidade())});
        }

        JTable tabela = new JTable(modelo);
        tabela.setBackground(COR_CARTAO);
        tabela.setForeground(Color.WHITE);
        tabela.setRowHeight(45);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(COR_ROXO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabela.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tabela.getTableHeader().setReorderingAllowed(false);

        // Estilizar Cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_FUNDO);
        header.setForeground(COR_TEXTO_SECUNDARIO);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(100, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 62, 65)));

        // Alinhamento das Colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 15, 0, 0));

        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);

        tabela.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(3).setMaxWidth(150);

        // ==========================================
        // 3. EVENTO DE DUPLO CLIQUE
        // ==========================================
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linhaClicada = tabela.getSelectedRow();
                    if (linhaClicada != -1) {
                        int idEstadio = (int) tabela.getValueAt(linhaClicada, 0);

                        Estadio estadioSelecionado = null;
                        for (Estadio est : CentralDeDados.getInstance().getEstadios()) {
                            if (est.getId() == idEstadio) {
                                estadioSelecionado = est;
                                break;
                            }
                        }

                        if (estadioSelecionado != null) {
                            JPanel parent = (JPanel) painelPrincipal.getParent();
                            parent.removeAll();
                            parent.add(new PainelEstadioDetalhe(estadioSelecionado).getPainelPrincipal(), BorderLayout.CENTER);
                            parent.revalidate();
                            parent.repaint();
                        }
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COR_CARTAO);

        cartaoTabela.add(scroll, BorderLayout.CENTER);
        painelCentro.add(cartaoTabela, BorderLayout.CENTER);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
    }

    // ==========================================
    // LÓGICA DE ADICIONAR NOVO ESTÁDIO
    // ==========================================
    private void abrirDialogoNovoEstadio() {
        Window janelaPai = SwingUtilities.getWindowAncestor(painelPrincipal);
        JDialog dialog = new JDialog((Frame) janelaPai, "Adicionar Novo Estádio", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(painelPrincipal);
        dialog.getContentPane().setBackground(COR_CARTAO);
        dialog.setLayout(new BorderLayout());

        // Painel Formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        formPanel.setBackground(COR_CARTAO);
        formPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        JLabel lblNome = criarLabelFormulario("Nome do Estádio:");
        JTextField txtNome = criarInputFormulario();

        JLabel lblCidade = criarLabelFormulario("Cidade:");
        JTextField txtCidade = criarInputFormulario();

        JLabel lblCapacidade = criarLabelFormulario("Capacidade (Lotação):");
        JTextField txtCapacidade = criarInputFormulario();

        formPanel.add(lblNome); formPanel.add(txtNome);
        formPanel.add(lblCidade); formPanel.add(txtCidade);
        formPanel.add(lblCapacidade); formPanel.add(txtCapacidade);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Painel Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(COR_CARTAO);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(45, 47, 56)));

        JButton btnCancelar = criarBotaoRedondo("Cancelar", new Color(60, 62, 65), Color.WHITE);
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnGuardar = criarBotaoRedondo("Guardar Estádio", COR_ROXO, Color.WHITE);
        btnGuardar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                String cidade = txtCidade.getText().trim();
                int capacidade = Integer.parseInt(txtCapacidade.getText().trim());

                if (nome.isEmpty() || cidade.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Por favor, preencha o Nome e a Cidade.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Gerar ID Automático (Procura o maior ID atual e soma 1)
                int novoId = 1;
                for (Estadio est : CentralDeDados.getInstance().getEstadios()) {
                    if (est.getId() >= novoId) novoId = est.getId() + 1;
                }

                // Criar o novo Estádio e adicionar à lista
                Estadio novoEstadio = new Estadio(novoId, nome, cidade, capacidade);
                CentralDeDados.getInstance().getEstadios().add(novoEstadio);

                // Adicionar à tabela instantaneamente
                modelo.addRow(new Object[]{novoEstadio.getId(), novoEstadio.getNome(), novoEstadio.getCidade(), String.format("%,d", novoEstadio.getCapacidade())});

                // Guardar no ficheiro estadios.txt
                guardarEstadioNoFicheiro(novoEstadio);

                JOptionPane.showMessageDialog(dialog, "Estádio guardado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "A capacidade tem de ser um número inteiro!", "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void guardarEstadioNoFicheiro(Estadio e) {
        // O "true" no FileWriter faz com que escreva na linha de baixo sem apagar os que já existem
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("estadios.txt", true))) {
            String linha = e.getId() + ";" + e.getNome() + ";" + e.getCidade() + ";" + e.getCapacidade();
            bw.write(linha);
            bw.newLine();
        } catch (IOException ex) {
            System.out.println("Erro ao guardar estádio no ficheiro: " + ex.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS UI AUXILIARES
    // ==========================================
    private JLabel criarLabelFormulario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    private JTextField criarInputFormulario() {
        JTextField txt = new JTextField();
        txt.setBackground(COR_FUNDO);
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 62, 65), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JButton criarBotaoRedondo(String texto, Color corFundo, Color corTexto) {
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
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(corTexto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    // ==========================================
    // CLASSE INTERNA: Painel Arredondado
    // ==========================================
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(backgroundColor);
            graphics.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        }
    }
}