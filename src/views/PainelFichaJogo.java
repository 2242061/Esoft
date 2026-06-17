package views;

import model.Arbitro;
import model.Jogador;
import model.Jogo;
import model.Alojamento;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PainelFichaJogo {
    private JPanel painelPrincipal;
    private Jogo jogo;
    private boolean modoVisualizacao;
    private JList<String> listaJogadoresCasa, listaJogadoresFora;

    private JComboBox<String> cbArbitroPrincipal, cbAss1, cbAss2, cbVar;
    private JComboBox<String> cbAlojamentoCasa, cbAlojamentoFora;

    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);

    // ==========================================
    // 1. CONSTRUTOR
    // ==========================================
    public PainelFichaJogo(Jogo jogo, boolean modoVisualizacao) {
        this.jogo = jogo;
        this.modoVisualizacao = modoVisualizacao;

        // Forçar cores escuras mesmo quando as comboboxes estão bloqueadas
        UIManager.put("ComboBox.disabledBackground", COR_FUNDO);
        UIManager.put("ComboBox.disabledForeground", Color.WHITE);

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // TOPO
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);

        JButton btnVoltar = criarBotaoRedondo("<- Voltar", COR_CARTAO, Color.WHITE);
        btnVoltar.addActionListener(e -> {
            JPanel parent = (JPanel) painelPrincipal.getParent();
            parent.removeAll();
            parent.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        });
        painelTopo.add(btnVoltar, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("FICHA: " + jogo.getEquipaCasa().getNome() + " vs " + jogo.getEquipaFora().getNome(), SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        painelTopo.add(lblTitulo, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // CENTRO
        JPanel painelCentro = new JPanel(new GridLayout(1, 3, 30, 0));
        painelCentro.setBackground(COR_FUNDO);

        // Inicializar Listas e Dropdowns
        cbArbitroPrincipal = criarComboArbitros();
        cbAss1 = criarComboArbitros();
        cbAss2 = criarComboArbitros();
        cbVar = criarComboArbitros();

        cbAlojamentoCasa = criarComboAlojamentos();
        cbAlojamentoFora = criarComboAlojamentos();

        listaJogadoresCasa = criarListaPlantel(jogo.getEquipaCasa().getNome());
        listaJogadoresFora = criarListaPlantel(jogo.getEquipaFora().getNome());

        if (modoVisualizacao) {
            // Carregar os dados de arbitragem guardados
            List<String> dados = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 3);
            if (dados.size() >= 4) {
                cbArbitroPrincipal.removeAllItems(); cbArbitroPrincipal.addItem(dados.get(0));
                cbAss1.removeAllItems(); cbAss1.addItem(dados.get(1));
                cbAss2.removeAllItems(); cbAss2.addItem(dados.get(2));
                cbVar.removeAllItems(); cbVar.addItem(dados.get(3));
            }

            // Bloquear os controlos
            listaJogadoresCasa.setEnabled(false);
            listaJogadoresFora.setEnabled(false);
            cbArbitroPrincipal.setEnabled(false);
            cbAss1.setEnabled(false);
            cbAss2.setEnabled(false);
            cbVar.setEnabled(false);
            cbAlojamentoCasa.setEnabled(false);
            cbAlojamentoFora.setEnabled(false);
        }

        painelCentro.add(wrapPlantel(listaJogadoresCasa, "Titulares (Casa)"));
        painelCentro.add(criarPainelArbitragem());
        painelCentro.add(wrapPlantel(listaJogadoresFora, "Titulares (Fora)"));

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
    }

    // ==========================================
    // 2. CONSTRUTORES DE UI (PAINÉIS E LISTAS)
    // ==========================================
    private JPanel wrapPlantel(JList<String> lista, String titulo) {
        RoundedPanel pnl = new RoundedPanel(15, COR_CARTAO);
        pnl.setLayout(new BorderLayout(0, 15));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(new JScrollPane(lista), BorderLayout.CENTER);
        return pnl;
    }

    private JList<String> criarListaPlantel(String nomeEquipa) {
        DefaultListModel<String> modelo = new DefaultListModel<>();

        if (modoVisualizacao) {
            int pos = nomeEquipa.equals(jogo.getEquipaCasa().getNome()) ? 1 : 2;
            List<String> salvos = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), pos);
            for (String nome : salvos) modelo.addElement(nome);
        } else {
            for (Jogador j : CentralDeDados.getInstance().getJogadores()) {
                if (j.getPais().equals(nomeEquipa)) {
                    modelo.addElement(" ("+ j.getNumeroCamisola() + ") - " +j.getNome() + " (" + j.getPosicao() + ")");
                }
            }
        }

        JList<String> lista = new JList<>(modelo);
        lista.setBackground(COR_FUNDO);
        lista.setForeground(Color.WHITE);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        if (modoVisualizacao) {
            lista.clearSelection();
            lista.setSelectionModel(new DefaultListSelectionModel() {
                @Override public void setSelectionInterval(int index0, int index1) { }
            });
            lista.setSelectionBackground(COR_FUNDO);
            lista.setSelectionForeground(Color.WHITE);
        } else {
            lista.setSelectionBackground(COR_ROXO);
            lista.setSelectionForeground(Color.WHITE);
        }

        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return lista;
    }

    private JPanel criarPainelArbitragem() {
        RoundedPanel pnl = new RoundedPanel(15, COR_CARTAO);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Alojamentos
        pnl.add(criarLabelFormulario("Hotel " + jogo.getEquipaCasa().getNome() + ":"));
        pnl.add(cbAlojamentoCasa);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(criarLabelFormulario("Hotel " + jogo.getEquipaFora().getNome() + ":"));
        pnl.add(cbAlojamentoFora);
        pnl.add(Box.createVerticalStrut(20));

        // Arbitragem
        pnl.add(criarLabelFormulario("Árbitro Principal:")); pnl.add(cbArbitroPrincipal);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(criarLabelFormulario("Ass. 1:")); pnl.add(cbAss1);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(criarLabelFormulario("Ass. 2:")); pnl.add(cbAss2);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(criarLabelFormulario("VAR:")); pnl.add(cbVar);

        if (!modoVisualizacao) {
            JButton btnGuardar = criarBotaoRedondo("Guardar Ficha", COR_ROXO, Color.WHITE);
            btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnGuardar.addActionListener(e -> {

                if (listaJogadoresCasa.getSelectedValuesList().size() != 11 ||
                        listaJogadoresFora.getSelectedValuesList().size() != 11) {
                    JOptionPane.showMessageDialog(painelPrincipal, "Erro: Selecione exatamente 11 titulares por equipa!");
                    return;
                }

                if (cbArbitroPrincipal.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(painelPrincipal, "Erro: Selecione a equipa de arbitragem!");
                    return;
                }

                if (cbAlojamentoCasa.getSelectedItem() == null || cbAlojamentoFora.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(painelPrincipal, "Erro: Escolha os hotéis para ambas as equipas!");
                    return;
                }

                if (cbAlojamentoCasa.getSelectedItem().equals(cbAlojamentoFora.getSelectedItem())) {
                    JOptionPane.showMessageDialog(painelPrincipal, "As equipas não podem ficar no mesmo alojamento!", "Risco de Segurança", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String arbitrosSelecionados = cbArbitroPrincipal.getSelectedItem().toString() + "," +
                        cbAss1.getSelectedItem().toString() + "," +
                        cbAss2.getSelectedItem().toString() + "," +
                        cbVar.getSelectedItem().toString();

                guardarFichaNoFicheiro(jogo.getId(), listaJogadoresCasa.getSelectedValuesList(),
                        listaJogadoresFora.getSelectedValuesList(), arbitrosSelecionados);

                JOptionPane.showMessageDialog(painelPrincipal, "Ficha guardada com sucesso!");

                JPanel parent = (JPanel) painelPrincipal.getParent();
                parent.removeAll();
                parent.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
                // ---------------------------------------------
            });
            pnl.add(Box.createVerticalStrut(20));
            pnl.add(btnGuardar);
        } else {
            JLabel lblGuardado = new JLabel("Ficha já guardada");
            lblGuardado.setForeground(Color.GREEN);
            lblGuardado.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnl.add(Box.createVerticalStrut(15));
            pnl.add(lblGuardado);
            pnl.add(Box.createVerticalStrut(15));

            JButton btnApitoFinal = criarBotaoRedondo("Apito Final", new Color(46, 204, 113), Color.WHITE);
            btnApitoFinal.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnApitoFinal.addActionListener(e -> {
                JPanel parent = (JPanel) painelPrincipal.getParent();
                parent.removeAll();
                parent.add(new PainelEstatisticasJogo(jogo).getPainelPrincipal(), BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
            });
            pnl.add(btnApitoFinal);
        }
        return pnl;
    }

    // ==========================================
    // 3. COMBOBOXES E ESTILOS
    // ==========================================
    private JComboBox<String> criarComboArbitros() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        aplicarTemaEscuroComboBox(cb);

        if (!modoVisualizacao) {
            String paisCasa = jogo.getEquipaCasa().getNome();
            String paisFora = jogo.getEquipaFora().getNome();

            for (Arbitro a : CentralDeDados.getInstance().getArbitros()) {
                if (!a.getNacionalidade().equalsIgnoreCase(paisCasa) &&
                        !a.getNacionalidade().equalsIgnoreCase(paisFora)) {
                    cb.addItem(a.getNome() + " (" + a.getNacionalidade() + ")");
                }
            }
        }
        return cb;
    }

    private JComboBox<String> criarComboAlojamentos() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        aplicarTemaEscuroComboBox(cb);

        if (!modoVisualizacao) {
            String cidadeDoJogo = jogo.getEstadio().getCidade();

            for (Alojamento a : CentralDeDados.getInstance().getAlojamentos()) {
                if (a.getCidade().equalsIgnoreCase(cidadeDoJogo)) {
                    cb.addItem(a.getNomeHotel() + " (" + a.getCidade() + ")");
                }
            }
        }
        return cb;
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

    // ==========================================
    // 4. AUXILIARES
    // ==========================================
    private JLabel criarLabelFormulario(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COR_TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
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
        btn.setBorderPainted(false);
        btn.setForeground(corTexto);
        return btn;
    }

    private void guardarFichaNoFicheiro(int id, List<String> casa, List<String> fora, String arb) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("fichas_jogo.txt", true))) {
            bw.write(id + ";" + String.join(",", casa) + ";" + String.join(",", fora) + ";" + arb);
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(painelPrincipal, "Erro ao guardar ficheiro!");
        }
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    // ==========================================
    // 5. CLASSES INTERNAS
    // ==========================================
    class RoundedPanel extends JPanel {
        private int r;
        private Color c;

        public RoundedPanel(int r, Color c) {
            this.r = r;
            this.c = c;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
        }
    }
}