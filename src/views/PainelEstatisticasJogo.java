package views;

import model.Jogo;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class PainelEstatisticasJogo {
    private JPanel painelPrincipal;
    private Jogo jogo;

    private JTextField txtMinuto;
    private JComboBox<String> cbJogadores;
    private JComboBox<String> cbEventos;

    private JTable tabelaTimeline;
    private DefaultTableModel modeloTimeline;

    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_VERDE = new Color(46, 204, 113);

    public PainelEstatisticasJogo(Jogo jogo) {
        this.jogo = jogo;

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // TOPO
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);

        JButton btnVoltar = criarBotaoPersonalizado("<- Voltar", COR_CARTAO, Color.WHITE);
        btnVoltar.addActionListener(e -> {
            JPanel parent = (JPanel) painelPrincipal.getParent();
            parent.removeAll();
            parent.add(new PainelFichaJogo(jogo, true).getPainelPrincipal(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        });
        painelTopo.add(btnVoltar, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Estatísticas: " + jogo.getEquipaCasa().getNome() + " vs " + jogo.getEquipaFora().getNome(), SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        painelTopo.add(lblTitulo, BorderLayout.CENTER);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // CENTRO
        JPanel painelCentro = new JPanel(new BorderLayout(0, 20));
        painelCentro.setBackground(COR_FUNDO);

        // 1. Formulário de Eventos (Com Dark Mode)
        RoundedPanel painelFormulario = new RoundedPanel(15, COR_CARTAO);
        painelFormulario.setLayout(new BorderLayout());
        painelFormulario.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel linhaInputs = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        linhaInputs.setOpaque(false);

        JLabel lblMin = new JLabel("Minuto:"); lblMin.setForeground(Color.WHITE);
        txtMinuto = new JTextField(4);
        txtMinuto.setPreferredSize(new Dimension(50, 35));
        txtMinuto.setBackground(COR_FUNDO); txtMinuto.setForeground(Color.WHITE);
        txtMinuto.setBorder(BorderFactory.createLineBorder(new Color(60, 62, 65)));

        JLabel lblJog = new JLabel("Jogador:"); lblJog.setForeground(Color.WHITE);
        cbJogadores = new JComboBox<>();
        aplicarTemaEscuroComboBox(cbJogadores); // Aplica as cores corretas!
        cbJogadores.setPreferredSize(new Dimension(280, 35));
        carregarJogadoresDaFicha();

        JLabel lblEvt = new JLabel("Evento:"); lblEvt.setForeground(Color.WHITE);
        String[] eventos = {"Golo", "Assistência", "Cartão Amarelo", "Cartão Vermelho", "Defesa", "Falta Cometida", "Falta Sofrida"};
        cbEventos = new JComboBox<>(eventos);
        aplicarTemaEscuroComboBox(cbEventos); // Aplica as cores corretas!
        cbEventos.setPreferredSize(new Dimension(150, 35));

        linhaInputs.add(lblMin); linhaInputs.add(txtMinuto);
        linhaInputs.add(lblJog); linhaInputs.add(cbJogadores);
        linhaInputs.add(lblEvt); linhaInputs.add(cbEventos);

        JPanel linhaBotao = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        linhaBotao.setOpaque(false);

        JButton btnAdicionarEvento = criarBotaoPersonalizado("Registar Evento", COR_ROXO, Color.WHITE);
        btnAdicionarEvento.setPreferredSize(new Dimension(200, 40));
        btnAdicionarEvento.addActionListener(e -> registarEventoNaTimeline());

        linhaBotao.add(btnAdicionarEvento);

        painelFormulario.add(linhaInputs, BorderLayout.CENTER);
        painelFormulario.add(linhaBotao, BorderLayout.SOUTH);

        painelCentro.add(painelFormulario, BorderLayout.NORTH);

        // 2. Timeline
        String[] colunas = {"Minuto", "Equipa", "Jogador", "Evento"};
        modeloTimeline = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaTimeline = new JTable(modeloTimeline);
        estilizarTabela(tabelaTimeline);

        JScrollPane scrollPane = new JScrollPane(tabelaTimeline);
        scrollPane.getViewport().setBackground(COR_CARTAO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_CARTAO));
        painelCentro.add(scrollPane, BorderLayout.CENTER);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        // BASE
        JPanel painelBase = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBase.setBackground(COR_FUNDO);

        JButton btnFinalizar = criarBotaoPersonalizado("Guardar Resultado e Estatísticas", COR_VERDE, Color.WHITE);
        btnFinalizar.setPreferredSize(new Dimension(300, 45));
        btnFinalizar.addActionListener(e -> processarEFinalizarJogo());

        painelBase.add(btnFinalizar);
        painelPrincipal.add(painelBase, BorderLayout.SOUTH);
    }

    private void carregarJogadoresDaFicha() {
        List<String> casa = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 1);
        List<String> fora = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 2);

        for (String j : casa) cbJogadores.addItem("[" + jogo.getEquipaCasa().getNome() + "] " + limparNome(j));
        for (String j : fora) cbJogadores.addItem("[" + jogo.getEquipaFora().getNome() + "] " + limparNome(j));
    }

    private String limparNome(String nomeBruto) {
        // Transforma "(13) - Guillermo Ochoa (Guarda-Redes)" em "Guillermo Ochoa"
        try {
            return nomeBruto.split(" - ")[1].split(" \\(")[0].trim();
        } catch (Exception e) {
            return nomeBruto;
        }
    }

    private void registarEventoNaTimeline() {
        String minutoStr = txtMinuto.getText().trim();
        if (!minutoStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(painelPrincipal, "O minuto tem de ser numérico!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int minuto = Integer.parseInt(minutoStr);
        if (minuto < 1 || minuto > 130) {
            JOptionPane.showMessageDialog(painelPrincipal, "Minuto inválido! Deve estar entre 1 e 130.", "Tempo Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selecaoJogador = (String) cbJogadores.getSelectedItem();
        String evento = (String) cbEventos.getSelectedItem();

        String equipa = selecaoJogador.substring(1, selecaoJogador.indexOf("]"));
        String nomeJogador = selecaoJogador.substring(selecaoJogador.indexOf("]") + 2);

        modeloTimeline.addRow(new Object[]{minuto + "'", equipa, nomeJogador, evento});

        txtMinuto.setText("");
        txtMinuto.requestFocus();
    }

    private void processarEFinalizarJogo() {
        if (modeloTimeline.getRowCount() == 0) {
            int resp = JOptionPane.showConfirmDialog(painelPrincipal, "A tabela de eventos está vazia (0-0). Deseja finalizar o jogo mesmo assim?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (resp != JOptionPane.YES_OPTION) return;
        }

        int golosCasa = 0;
        int golosFora = 0;

        for (int i = 0; i < modeloTimeline.getRowCount(); i++) {
            String equipa = (String) modeloTimeline.getValueAt(i, 1);
            String evento = (String) modeloTimeline.getValueAt(i, 3);
            if (evento.equals("Golo")) {
                if (equipa.equals(jogo.getEquipaCasa().getNome())) golosCasa++;
                else golosFora++;
            }
        }

        String mensagem = "Resultado Final calculado:\n\n" +
                jogo.getEquipaCasa().getNome() + " " + golosCasa + " - " +
                golosFora + " " + jogo.getEquipaFora().getNome() + "\n\n" +
                "Deseja guardar este resultado e as estatísticas definitivamente?";

        int confirmacao = JOptionPane.showConfirmDialog(painelPrincipal, mensagem, "Confirmar Apito Final", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {

            // 1. GUARDAR O RESULTADO GERAL
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("resultados.txt", true))) {
                bw.write(jogo.getId() + ";" + golosCasa + ";" + golosFora);
                bw.newLine();
            } catch (Exception ex) { }

            // 2. GUARDAR ESTATÍSTICAS INDIVIDUAIS
            java.util.Map<String, int[]> statsPorJogador = new java.util.HashMap<>();

            for (int i = 0; i < modeloTimeline.getRowCount(); i++) {
                String equipa = (String) modeloTimeline.getValueAt(i, 1);
                String nomeJogador = (String) modeloTimeline.getValueAt(i, 2);
                String evento = (String) modeloTimeline.getValueAt(i, 3);

                String chave = equipa + "_" + nomeJogador;
                statsPorJogador.putIfAbsent(chave, new int[]{0, 0, 0});

                if (evento.equals("Golo")) statsPorJogador.get(chave)[0]++;
                else if (evento.equals("Assistência")) statsPorJogador.get(chave)[1]++;
                else if (evento.equals("Cartão Amarelo")) statsPorJogador.get(chave)[2] += 1;
                else if (evento.equals("Cartão Vermelho")) statsPorJogador.get(chave)[2] += 2;
            }

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("estatisticas.txt", true))) {
                String dataHoje = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int novaIdEstatistica = (int) (Math.random() * 10000);

                for (String chave : statsPorJogador.keySet()) {
                    String equipa = chave.split("_")[0];
                    String nomeJogador = chave.split("_")[1];
                    int[] stats = statsPorJogador.get(chave);

                    // Descobrir o ID garantindo correspondência exata!
                    int idJogador = -1;
                    for (model.Jogador j : CentralDeDados.getInstance().getJogadores()) {
                        if (j.getNome().equalsIgnoreCase(nomeJogador) && j.getPais().equalsIgnoreCase(equipa)) {
                            idJogador = j.getId();
                            break;
                        }
                    }

                    if (idJogador != -1) {
                        String adversario = equipa.equals(jogo.getEquipaCasa().getNome()) ? jogo.getEquipaFora().getNome() : jogo.getEquipaCasa().getNome();
                        double ratingGerado = 6.0 + (stats[0] * 1.5) + (stats[1] * 0.5) - (stats[2] * 1.0);
                        if (ratingGerado > 10.0) ratingGerado = 10.0;

                        bw.write(novaIdEstatistica + ";" + idJogador + ";" + adversario + ";" + stats[0] + ";" + stats[1] + ";" + stats[2] + ";" + String.format(java.util.Locale.US, "%.1f", ratingGerado) + ";" + dataHoje);
                        bw.newLine();
                        novaIdEstatistica++;
                    } else {
                        System.out.println("Aviso: Jogador não encontrado na BD: " + nomeJogador);
                    }
                }
            } catch (Exception ex) {
            System.out.println("Erro ao gravar estatísticas: " + ex.getMessage());
        }

        CentralDeDados.getInstance().recarregarEstatisticas();

        JOptionPane.showMessageDialog(painelPrincipal, "Jogo finalizado e estatísticas guardadas na Base de Dados!");
        JPanel parent = (JPanel) painelPrincipal.getParent();
        parent.removeAll();
        parent.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
}


    private void aplicarTemaEscuroComboBox(JComboBox<String> cb) {
        cb.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▼");
                btn.setBackground(COR_CARTAO);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return btn;
            }
            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(COR_FUNDO);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        cb.setBackground(COR_FUNDO);
        cb.setForeground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(new Color(60, 62, 65)));
        cb.setOpaque(true);

        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setOpaque(true);
                label.setBorder(new EmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    label.setBackground(COR_ROXO);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(COR_FUNDO);
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });
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
        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
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
        return btn;
    }

    public JPanel getPainelPrincipal() { return painelPrincipal; }

    class RoundedPanel extends JPanel {
        private int r; private Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
        }
    }
}