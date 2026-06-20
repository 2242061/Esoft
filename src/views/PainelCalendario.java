package views;

import model.Equipa;
import model.Estadio;
import model.Jogo;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PainelCalendario {
    private JPanel painelPrincipal;
    private JPanel painelListaJogos;

    // Paleta de Cores Premium
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);
    private final Color COR_VERDE_BOTAO = new Color(46, 125, 50);

    public PainelCalendario() {
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);

        // ==========================================
        // 1. TOPO: Título e Botão de Agendar
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);
        painelTopo.setBorder(new EmptyBorder(30, 40, 20, 40));

        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1));
        pnlTitulos.setOpaque(false);
        JLabel lblTitulo = new JLabel("CALENDÁRIO DE JOGOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel lblSub = new JLabel("Gere as partidas, regista resultados e emite bilhetes.");
        lblSub.setForeground(COR_TEXTO_SECUNDARIO);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        painelTopo.add(pnlTitulos, BorderLayout.WEST);

        JButton btnAgendar = criarBotaoRedondo("+ Agendar Novo Jogo", COR_ROXO, Color.WHITE);

        btnAgendar.setVisible(CentralDeDados.getInstance().isAdmin());
        btnAgendar.addActionListener(e -> abrirDialogoNovoJogo());

        JPanel pnlBotaoTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pnlBotaoTopo.setOpaque(false);
        pnlBotaoTopo.add(btnAgendar);
        painelTopo.add(pnlBotaoTopo, BorderLayout.EAST);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTRO: Lista de Cartões de Jogo
        // ==========================================
        painelListaJogos = new JPanel();
        painelListaJogos.setLayout(new BoxLayout(painelListaJogos, BoxLayout.Y_AXIS));
        painelListaJogos.setBackground(COR_FUNDO);
        painelListaJogos.setBorder(new EmptyBorder(10, 40, 40, 40));

        carregarJogosUI();

        JScrollPane scrollPane = new JScrollPane(painelListaJogos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COR_FUNDO);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarJogosUI() {
        painelListaJogos.removeAll();
        List<Jogo> jogos = CentralDeDados.getInstance().getJogos();

        if (jogos.isEmpty()) {
            JLabel lblVazio = new JLabel("Ainda não existem jogos agendados.");
            lblVazio.setForeground(COR_TEXTO_SECUNDARIO);
            lblVazio.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelListaJogos.add(Box.createVerticalStrut(50));
            painelListaJogos.add(lblVazio);
        } else {
            for (Jogo j : jogos) {
                painelListaJogos.add(criarCartaoJogo(j));
                painelListaJogos.add(Box.createVerticalStrut(15));
            }
        }

        painelListaJogos.revalidate();
        painelListaJogos.repaint();
    }

    // ==========================================
    // LÓGICA DE AGENDAR NOVO JOGO
    // ==========================================
    private void abrirDialogoNovoJogo() {
        Window janelaPai = SwingUtilities.getWindowAncestor(painelPrincipal);
        JDialog dialog = new JDialog((Frame) janelaPai, "Agendar Partida", true);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(painelPrincipal);
        dialog.getContentPane().setBackground(COR_CARTAO);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formPanel.setBackground(COR_CARTAO);
        formPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        List<String> nomesEquipas = new ArrayList<>();
        for(Equipa e : CentralDeDados.getInstance().getEquipas()) nomesEquipas.add(e.getNome());

        List<String> nomesEstadios = new ArrayList<>();
        for(Estadio e : CentralDeDados.getInstance().getEstadios()) nomesEstadios.add(e.getNome());

        JLabel lblCasa = criarLabelFormulario("Equipa Casa:");
        JComboBox<String> cbCasa = criarComboBox(nomesEquipas);

        JLabel lblFora = criarLabelFormulario("Equipa Fora:");
        JComboBox<String> cbFora = criarComboBox(new ArrayList<>());

        JLabel lblEstadio = criarLabelFormulario("Estádio:");
        JComboBox<String> cbEstadio = criarComboBox(nomesEstadios);

        JLabel lblData = criarLabelFormulario("Data (Ex: 15/06/2026):");
        JTextField txtData = criarInputFormulario();

        JLabel lblHora = criarLabelFormulario("Hora (Ex: 20:00):");
        JTextField txtHora = criarInputFormulario();

        cbCasa.addActionListener(e -> {
            String nomeCasaSelecionada = (String) cbCasa.getSelectedItem();
            if (nomeCasaSelecionada != null) {
                Equipa eqCasa = CentralDeDados.getInstance().getEquipas().stream()
                        .filter(eq -> eq.getNome().equals(nomeCasaSelecionada))
                        .findFirst().orElse(null);

                if (eqCasa != null) {
                    cbFora.removeAllItems();
                    for (Equipa eq : CentralDeDados.getInstance().getEquipas()) {
                        if (eq.getGrupo().equals(eqCasa.getGrupo()) && !eq.getNome().equals(eqCasa.getNome())) {
                            cbFora.addItem(eq.getNome());
                        }
                    }
                }
            }
        });

        cbCasa.setSelectedIndex(0);

        formPanel.add(lblCasa); formPanel.add(cbCasa);
        formPanel.add(lblFora); formPanel.add(cbFora);
        formPanel.add(lblEstadio); formPanel.add(cbEstadio);
        formPanel.add(lblData); formPanel.add(txtData);
        formPanel.add(lblHora); formPanel.add(txtHora);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(COR_CARTAO);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(45, 47, 56)));

        JButton btnCancelar = criarBotaoRedondo("Cancelar", new Color(60, 62, 65), Color.WHITE);
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnGuardar = criarBotaoRedondo("Agendar", COR_ROXO, Color.WHITE);
        btnGuardar.addActionListener(e -> {
            String nomeCasa = (String) cbCasa.getSelectedItem();
            String nomeFora = (String) cbFora.getSelectedItem();
            String nomeEstadio = (String) cbEstadio.getSelectedItem();
            String data = txtData.getText().trim();
            String hora = txtHora.getText().trim();

            if (nomeFora == null) {
                JOptionPane.showMessageDialog(dialog, "Erro ao selecionar equipa adversária.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(dialog, "Data inválida!\nUse o formato DD/MM/YYYY (ex: 15/06/2026).", "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                JOptionPane.showMessageDialog(dialog, "Hora inválida!\nUse o formato HH:MM (ex: 20:30).", "Erro de Formatação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Equipa eqCasa = CentralDeDados.getInstance().getEquipas().stream().filter(eq -> eq.getNome().equals(nomeCasa)).findFirst().orElse(null);
            Equipa eqFora = CentralDeDados.getInstance().getEquipas().stream().filter(eq -> eq.getNome().equals(nomeFora)).findFirst().orElse(null);
            Estadio est = CentralDeDados.getInstance().getEstadios().stream().filter(es -> es.getNome().equals(nomeEstadio)).findFirst().orElse(null);

            if (eqCasa != null && eqFora != null && est != null) {
                int novoId = 1;
                for (Jogo j : CentralDeDados.getInstance().getJogos()) {
                    if (j.getId() >= novoId) novoId = j.getId() + 1;
                }

                Jogo novoJogo = new Jogo(novoId, eqCasa, eqFora, est, data, hora);
                CentralDeDados.getInstance().getJogos().add(novoJogo);
                guardarJogoNoFicheiro(novoJogo);

                JOptionPane.showMessageDialog(dialog, "Jogo Agendado com Sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();

                carregarJogosUI();
            }
        });

        btnPanel.add(btnCancelar);
        btnPanel.add(btnGuardar);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void guardarJogoNoFicheiro(Jogo j) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("jogos.txt", true))) {
            String linha = j.getId() + ";" + j.getEquipaCasa().getNome() + ";" + j.getEquipaFora().getNome() + ";"
                    + j.getEstadio().getNome() + ";" + j.getData() + ";" + j.getHora();
            bw.write(linha);
            bw.newLine();
        } catch (IOException ex) {
            System.out.println("Erro ao guardar jogo no ficheiro: " + ex.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE DESIGN UI E ESTADOS DO JOGO
    // ==========================================

    private JPanel criarCartaoJogo(Jogo j) {
        RoundedPanel cartao = new RoundedPanel(15, COR_CARTAO);
        cartao.setLayout(new BorderLayout());
        cartao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        cartao.setBorder(new EmptyBorder(15, 25, 15, 25));

        // --- VERIFICAR ESTADO DO JOGO ---
        int[] resultado = CentralDeDados.getInstance().getResultadoJogo(j.getId());
        boolean jogoTerminado = (resultado != null);
        boolean jaTemFicha = CentralDeDados.getInstance().existeFichaParaJogo(j.getId());

        // LADO ESQUERDO: Data e Hora
        JPanel pnlData = new JPanel(new GridLayout(2, 1));
        pnlData.setOpaque(false);
        pnlData.setPreferredSize(new Dimension(120, 60));

        JLabel lblData = new JLabel(j.getData());
        lblData.setForeground(COR_TEXTO_SECUNDARIO);
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblHora = new JLabel(jogoTerminado ? "FINAL" : j.getHora()); // Se terminou, diz FINAL
        lblHora.setForeground(jogoTerminado ? new Color(220, 53, 69) : Color.WHITE); // Vermelho se terminou
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 22));

        pnlData.add(lblData);
        pnlData.add(lblHora);
        cartao.add(pnlData, BorderLayout.WEST);

        // CENTRO: Equipas, Estádio e Resultado
        JPanel pnlCentro = new JPanel(new GridLayout(2, 1));
        pnlCentro.setOpaque(false);

        String textoEquipas = j.getEquipaCasa().getNome().toUpperCase() + "  vs  " + j.getEquipaFora().getNome().toUpperCase();
        if (jogoTerminado) {
            // Se o jogo acabou, injetamos os golos no meio dos nomes!
            textoEquipas = j.getEquipaCasa().getNome().toUpperCase() + "   " + resultado[0] + " - " + resultado[1] + "   " + j.getEquipaFora().getNome().toUpperCase();
        }

        JLabel lblEquipas = new JLabel(textoEquipas);
        lblEquipas.setForeground(jogoTerminado ? COR_VERDE_BOTAO : Color.WHITE); // Verde se terminou
        lblEquipas.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblEquipas.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblEstadio = new JLabel("" + j.getEstadio().getNome() + " (" + j.getEstadio().getCidade() + ")");
        lblEstadio.setForeground(COR_TEXTO_SECUNDARIO);
        lblEstadio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstadio.setHorizontalAlignment(SwingConstants.CENTER);

        pnlCentro.add(lblEquipas);
        pnlCentro.add(lblEstadio);
        cartao.add(pnlCentro, BorderLayout.CENTER);

        // LADO DIREITO: Botões Adaptados ao Estado
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlAcoes.setOpaque(false);

        if (!jogoTerminado) {
            // JOGO AINDA NÃO COMEÇOU
            String textoBotaoFicha = jaTemFicha ? "Ver Ficha" : "Ficha de Jogo";
            Color corBotaoFicha = jaTemFicha ? new Color(100, 100, 100) : new Color(0, 120, 215);

            JButton btnFicha = criarBotaoRedondo(textoBotaoFicha, corBotaoFicha, Color.WHITE);
            btnFicha.addActionListener(e -> {
                JPanel parent = (JPanel) painelPrincipal.getParent();
                parent.removeAll();
                parent.add(new PainelFichaJogo(j, jaTemFicha).getPainelPrincipal(), BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
            });
            pnlAcoes.add(btnFicha);

            // Só mostra bilhetes se não tiver terminado
            JButton btnBilhetes = criarBotaoRedondo("Bilhetes", new Color(40, 42, 54), Color.WHITE);
            btnBilhetes.setVisible(CentralDeDados.getInstance().isAdmin());
            btnBilhetes.addActionListener(e -> JOptionPane.showMessageDialog(painelPrincipal, "Estatísticas de bilhetes vendidos (em breve)."));
            pnlAcoes.add(btnBilhetes);

        } else {
            // JOGO TERMINADO
            JButton btnRelatorio = criarBotaoRedondo("Ver Relatório", COR_VERDE_BOTAO, Color.WHITE);
            btnRelatorio.addActionListener(e -> {
                JPanel parent = (JPanel) painelPrincipal.getParent();
                parent.removeAll();
                parent.add(new PainelRelatorioJogo(j).getPainelPrincipal(), BorderLayout.CENTER);
                parent.revalidate();
                parent.repaint();
            });
            pnlAcoes.add(btnRelatorio);

            JButton btnBilhetes = criarBotaoRedondo("Estat. Bilhetes", new Color(40, 42, 54), Color.WHITE);
            btnBilhetes.setVisible(CentralDeDados.getInstance().isAdmin());
            btnBilhetes.addActionListener(e -> JOptionPane.showMessageDialog(painelPrincipal, "Estatísticas finais de bilheteira (em breve)."));
            pnlAcoes.add(btnBilhetes);
        }

        cartao.add(pnlAcoes, BorderLayout.EAST);

        return cartao;
    }

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

    private JComboBox<String> criarComboBox(List<String> itens) {
        JComboBox<String> cb = new JComboBox<>(itens.toArray(new String[0]));
        cb.setBackground(Color.WHITE);
        cb.setForeground(Color.BLACK);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBorder(BorderFactory.createLineBorder(new Color(60, 62, 65), 1));
        return cb;
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JPanel getPainelPrincipal() { return painelPrincipal; }

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