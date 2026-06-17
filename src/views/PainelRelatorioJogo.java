package views;

import model.EstatisticaJogador;
import model.Jogador;
import model.Jogo;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class PainelRelatorioJogo {
    private JPanel painelPrincipal;
    private Jogo jogo;

    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);

    public PainelRelatorioJogo(Jogo jogo) {
        this.jogo = jogo;

        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);

        // Contentor principal que vai ter SCROLL para nada ficar cortado
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(COR_FUNDO);
        painelConteudo.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. DADOS DO JOGO
        int[] resultado = CentralDeDados.getInstance().getResultadoJogo(jogo.getId());
        List<String> fichaCasa = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 1);
        List<String> fichaFora = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 2);
        List<String> arbitragem = CentralDeDados.getInstance().getJogadoresGuardadosNaFicha(jogo.getId(), 3);

        String golosCasa = (resultado != null) ? String.valueOf(resultado[0]) : "?";
        String golosFora = (resultado != null) ? String.valueOf(resultado[1]) : "?";

        // ==========================================
        // TOPO: Botão Voltar e Placar
        // ==========================================
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(COR_FUNDO);
        painelTopo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton btnVoltar = criarBotaoRedondo("<- Voltar ao Calendário", COR_CARTAO, Color.WHITE);
        btnVoltar.addActionListener(e -> {
            JPanel parent = (JPanel) painelPrincipal.getParent();
            parent.removeAll();
            parent.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        });

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnVoltar);
        painelTopo.add(pnlBtn, BorderLayout.NORTH);

        JPanel pnlPlacar = new JPanel(new GridLayout(3, 1));
        pnlPlacar.setOpaque(false);

        JLabel lblTitulo = new JLabel("RELATÓRIO FINAL", SwingConstants.CENTER);
        lblTitulo.setForeground(COR_TEXTO_SECUNDARIO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblScore = new JLabel(jogo.getEquipaCasa().getNome() + "   " + golosCasa + " - " + golosFora + "   " + jogo.getEquipaFora().getNome(), SwingConstants.CENTER);
        lblScore.setForeground(new Color(46, 204, 113)); // Verde vitória
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 42));

        JLabel lblEstadio = new JLabel(jogo.getEstadio().getNome() + " | " + jogo.getData(), SwingConstants.CENTER);
        lblEstadio.setForeground(COR_TEXTO_SECUNDARIO);
        lblEstadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnlPlacar.add(lblTitulo);
        pnlPlacar.add(lblScore);
        pnlPlacar.add(lblEstadio);
        painelTopo.add(pnlPlacar, BorderLayout.CENTER);

        painelConteudo.add(painelTopo);
        painelConteudo.add(Box.createVerticalStrut(30)); // Espaçamento

        // ==========================================
        // DESTAQUES: Quem marcou e levou cartões
        // ==========================================
        JPanel pnlDestaques = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlDestaques.setOpaque(false);
        pnlDestaques.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        pnlDestaques.add(criarPainelEventos(jogo.getEquipaCasa().getNome(), fichaCasa, jogo.getEquipaFora().getNome()));
        pnlDestaques.add(criarPainelEventos(jogo.getEquipaFora().getNome(), fichaFora, jogo.getEquipaCasa().getNome()));

        painelConteudo.add(pnlDestaques);
        painelConteudo.add(Box.createVerticalStrut(30));

        // ==========================================
        // TITULARES: Plantéis Completos
        // ==========================================
        JPanel pnlTitulares = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTitulares.setOpaque(false);

        pnlTitulares.add(criarListaEquipa("11 Inicial - " + jogo.getEquipaCasa().getNome(), fichaCasa, jogo.getEquipaFora().getNome()));
        pnlTitulares.add(criarListaEquipa("11 Inicial - " + jogo.getEquipaFora().getNome(), fichaFora, jogo.getEquipaCasa().getNome()));

        painelConteudo.add(pnlTitulares);
        painelConteudo.add(Box.createVerticalStrut(30));

        // ==========================================
        // BASE: Equipa de Arbitragem
        // ==========================================
        if (arbitragem != null && arbitragem.size() >= 4) {
            RoundedPanel pnlArbitragem = new RoundedPanel(15, COR_CARTAO);
            pnlArbitragem.setLayout(new BorderLayout());
            pnlArbitragem.setBorder(new EmptyBorder(15, 20, 15, 20));
            pnlArbitragem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            JLabel lblArbTitulo = new JLabel("EQUIPA DE ARBITRAGEM", SwingConstants.CENTER);
            lblArbTitulo.setForeground(Color.WHITE);
            lblArbTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblArbTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
            pnlArbitragem.add(lblArbTitulo, BorderLayout.NORTH);

            JPanel pnlNomesArb = new JPanel(new GridLayout(1, 4, 10, 0));
            pnlNomesArb.setOpaque(false);

            pnlNomesArb.add(criarLabelArbitro("Principal", arbitragem.get(0)));
            pnlNomesArb.add(criarLabelArbitro("Assistente 1", arbitragem.get(1)));
            pnlNomesArb.add(criarLabelArbitro("Assistente 2", arbitragem.get(2)));
            pnlNomesArb.add(criarLabelArbitro("VAR", arbitragem.get(3)));

            pnlArbitragem.add(pnlNomesArb, BorderLayout.CENTER);
            painelConteudo.add(pnlArbitragem);
        }

        // Adicionar o conteúdo principal a um ScrollPane
        JScrollPane scrollPane = new JScrollPane(painelConteudo);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COR_FUNDO);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE UI ---

    private JPanel criarPainelEventos(String nomeEquipa, List<String> jogadores, String adversario) {
        RoundedPanel pnl = new RoundedPanel(15, COR_CARTAO);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblInfo = new JLabel("Destaques " + nomeEquipa);
        lblInfo.setForeground(COR_TEXTO_SECUNDARIO);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnl.add(lblInfo);
        pnl.add(Box.createVerticalStrut(10));

        boolean temEventos = false;
        for (String nomeBruto : jogadores) {
            String eventosStr = obterEventosFormatados(nomeBruto, adversario);
            if (!eventosStr.isEmpty()) {
                temEventos = true;
                JLabel lblEvt = new JLabel(limparNome(nomeBruto) + "  " + eventosStr);
                lblEvt.setForeground(Color.WHITE);
                lblEvt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                pnl.add(lblEvt);
                pnl.add(Box.createVerticalStrut(5));
            }
        }

        if (!temEventos) {
            JLabel lblVazio = new JLabel("Sem golos ou cartões.");
            lblVazio.setForeground(new Color(100, 100, 100));
            lblVazio.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            pnl.add(lblVazio);
        }

        return pnl;
    }

    private JPanel criarListaEquipa(String titulo, List<String> jogadores, String adversario) {
        RoundedPanel pnl = new RoundedPanel(15, COR_CARTAO);
        pnl.setLayout(new BorderLayout(0, 10));
        pnl.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 62, 65)));
        pnl.add(lbl, BorderLayout.NORTH);

        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (String nomeBruto : jogadores) {
            String eventos = obterEventosFormatados(nomeBruto, adversario);
            // Mostrar número, nome, posição e ícones
            modelo.addElement(nomeBruto + " " + eventos);
        }

        JList<String> lista = new JList<>(modelo);
        lista.setBackground(COR_CARTAO);
        lista.setForeground(Color.WHITE);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lista.setSelectionModel(new DefaultListSelectionModel() {
            @Override public void setSelectionInterval(int index0, int index1) { } // Desativa a seleção visual
        });

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COR_CARTAO);
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel criarLabelArbitro(String cargo, String nome) {
        JPanel pnl = new JPanel(new GridLayout(2, 1));
        pnl.setOpaque(false);
        JLabel lblCargo = new JLabel(cargo, SwingConstants.CENTER);
        lblCargo.setForeground(COR_TEXTO_SECUNDARIO);
        lblCargo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel lblNome = new JLabel(nome, SwingConstants.CENTER);
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnl.add(lblCargo);
        pnl.add(lblNome);
        return pnl;
    }

    // --- LÓGICA DE EVENTOS ---

    private String obterEventosFormatados(String nomeBruto, String adversario) {
        Jogador jogadorEncontrado = null;
        for (Jogador j : CentralDeDados.getInstance().getJogadores()) {
            // Verifica se o nome do jogador existe dentro da string bruta (ex: "(7) - Cristiano Ronaldo (Avançado)")
            if (nomeBruto.contains(j.getNome())) {
                jogadorEncontrado = j;
                break;
            }
        }

        if (jogadorEncontrado != null) {
            for (EstatisticaJogador stat : CentralDeDados.getInstance().getEstatisticasDoJogador(jogadorEncontrado.getId())) {
                if (stat.getAdversario().equalsIgnoreCase(adversario)) {
                    StringBuilder sb = new StringBuilder();
                    if (stat.getGolos() > 0) sb.append("GOLO (x").append(stat.getGolos()).append(") ");
                    if (stat.getCartoes() == 1) sb.append("Amarelo ");
                    if (stat.getCartoes() >= 2) sb.append("Vermelho ");
                    return sb.toString();
                }
            }
        }
        return "";
    }

    private String limparNome(String nomeBruto) {
        try {
            return nomeBruto.split(" - ")[1].split(" \\(")[0];
        } catch (Exception e) {
            return nomeBruto;
        }
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