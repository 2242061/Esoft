package views;

import model.Estadio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class PainelEstadioDetalhe {
    private JPanel painelPrincipal;
    private Estadio estadio;

    // Paleta de Cores Premium
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);

    // Cores das Zonas
    private final Color COR_NORTE = new Color(230, 110, 30);  // Laranja (Topo)
    private final Color COR_SUL = new Color(46, 125, 50);     // Verde (Fundo)
    private final Color COR_ESTE = new Color(114, 72, 232);   // Roxo (Direita)
    private final Color COR_OESTE = new Color(2, 119, 189);   // Azul (Esquerda)

    public PainelEstadioDetalhe(Estadio estadio) {
        this.estadio = estadio;

        painelPrincipal = new JPanel(new BorderLayout(30, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // TOPO: Título
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTopo.setBackground(COR_FUNDO);
        JLabel lblTitulo = new JLabel(estadio.getNome().toUpperCase());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        painelTopo.add(lblTitulo);
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // CENTRO: Divisão Esquerda (Info) e Direita (Mapa)
        JPanel painelCentro = new JPanel(new GridLayout(1, 2, 40, 0));
        painelCentro.setBackground(COR_FUNDO);

        // ==========================================
        // LADO ESQUERDO: Cartões de Informação
        // ==========================================
        // Usamos BorderLayout para garantir que os botões ficam presos no fundo e a info no topo
        JPanel painelEsquerdaContainer = new JPanel(new BorderLayout());
        painelEsquerdaContainer.setBackground(COR_FUNDO);

        // Usamos GridBagLayout para eliminar o "espaço preto" e forçar os cartões a esticarem a 100%
        JPanel painelEsquerdaInfo = new JPanel(new GridBagLayout());
        painelEsquerdaInfo.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0); // Espaçamento inferior entre cartões

        // Cartões
        painelEsquerdaInfo.add(criarCartaoModerno("LOCALIZAÇÃO", estadio.getCidade(), COR_OESTE), gbc);
        gbc.gridy++;
        painelEsquerdaInfo.add(criarCartaoModerno("LOTAÇÃO MÁXIMA TOTAL", String.format("%,d", estadio.getCapacidade()) + " lugares", COR_ROXO), gbc);
        gbc.gridy++;
        painelEsquerdaInfo.add(criarCartaoModerno("ID DO RECINTO", "Código: " + estadio.getId(), COR_SUL), gbc);
        gbc.gridy++;

        // Cartão Especial (Destaque)
        RoundedPanel cartaoDestaque = new RoundedPanel(15, new Color(38, 30, 74));
        cartaoDestaque.setLayout(new BorderLayout(15, 0));
        cartaoDestaque.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlTextosDestaque = new JPanel(new GridLayout(2, 1));
        pnlTextosDestaque.setOpaque(false);
        JLabel lblTitDest = new JLabel("INFRAESTRUTURA FIFA");
        lblTitDest.setForeground(Color.WHITE);
        lblTitDest.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblSubDest = new JLabel("Estádio aprovado para o Mundial 2026.");
        lblSubDest.setForeground(new Color(180, 180, 190));
        lblSubDest.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlTextosDestaque.add(lblTitDest);
        pnlTextosDestaque.add(lblSubDest);
        cartaoDestaque.add(pnlTextosDestaque, BorderLayout.CENTER);

        painelEsquerdaInfo.add(cartaoDestaque, gbc);

        painelEsquerdaContainer.add(painelEsquerdaInfo, BorderLayout.NORTH);

        // Botões no fundo da esquerda
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnVoltar = criarBotaoRedondo("Voltar", COR_CARTAO, Color.WHITE);
        btnVoltar.addActionListener(e -> {
            JPanel parent = (JPanel) painelPrincipal.getParent();
            parent.removeAll();
            parent.add(new PainelEstadios().getPainelPrincipal(), BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        });

        JButton btnSelecionar = criarBotaoRedondo("Marcar Jogo Aqui", COR_ROXO, Color.WHITE);

        // Caixa invisível para dar espaçamento entre botões
        painelBotoes.add(btnVoltar);
        painelBotoes.add(Box.createRigidArea(new Dimension(15, 0)));
        painelBotoes.add(btnSelecionar);

        painelEsquerdaContainer.add(painelBotoes, BorderLayout.SOUTH);

        painelCentro.add(painelEsquerdaContainer);

        // ==========================================
        // LADO DIREITO: Mapa do Estádio Interativo
        // ==========================================
        RoundedPanel painelDireita = new RoundedPanel(20, COR_CARTAO);
        painelDireita.setLayout(new BorderLayout());
        painelDireita.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cabeçalho do Mapa
        JPanel pnlMapaTopo = new JPanel(new GridLayout(2, 1));
        pnlMapaTopo.setOpaque(false);
        JLabel lblMapaTit = new JLabel("Mapa do Estádio (Horizontal)");
        lblMapaTit.setForeground(Color.WHITE);
        lblMapaTit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lblMapaSub = new JLabel("Clique numa zona colorida para ver a capacidade.");
        lblMapaSub.setForeground(COR_TEXTO_SECUNDARIO);
        lblMapaSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlMapaTopo.add(lblMapaTit);
        pnlMapaTopo.add(lblMapaSub);
        painelDireita.add(pnlMapaTopo, BorderLayout.NORTH);

        // Label Dinâmica que vai atualizar com os cliques
        JLabel lblInfoClicada = new JLabel("Nenhuma zona selecionada.", SwingConstants.CENTER);
        lblInfoClicada.setForeground(Color.WHITE);
        lblInfoClicada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfoClicada.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Desenho do Estádio HORIZONTAL
        JPanel painelGraficoEstadio = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // 1. Tornar o mapa HORIZONTAL e mais PEQUENO (80% da largura do painel)
                int extW = (int) (w * 0.80);
                int extH = (int) (extW * 0.65); // Aspect Ratio Horizontal (Oval deitada)

                // Centrar no ecrã
                int extX = (w - extW) / 2;
                int extY = (h - extH) / 2;

                // 2. Bancadas (Arcos) - Note que os ângulos mudaram para a nova orientação!
                g2d.setColor(COR_NORTE); g2d.fillArc(extX, extY, extW, extH, 45, 90);  // Topo (Bancada Central Norte)
                g2d.setColor(COR_SUL);   g2d.fillArc(extX, extY, extW, extH, 225, 90); // Fundo (Bancada Central Sul)
                g2d.setColor(COR_ESTE);  g2d.fillArc(extX, extY, extW, extH, -45, 90); // Direita (Atrás da baliza Este)
                g2d.setColor(COR_OESTE); g2d.fillArc(extX, extY, extW, extH, 135, 90); // Esquerda (Atrás da baliza Oeste)

                // Anel interior (Recorte central para o relvado)
                g2d.setColor(COR_CARTAO);
                g2d.fillOval(extX + 35, extY + 35, extW - 70, extH - 70);

                // 3. Relvado Central Horizontal
                int relvW = (int)(extW * 0.60); // Mais largo
                int relvH = (int)(extH * 0.40); // Mais achatado
                int relvX = (w - relvW) / 2;
                int relvY = (h - relvH) / 2;

                g2d.setColor(new Color(40, 130, 45)); // Verde relvado
                g2d.fillRoundRect(relvX, relvY, relvW, relvH, 5, 5);

                // Linhas do Relvado (Orientadas para um campo horizontal)
                g2d.setColor(new Color(255, 255, 255, 120));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(relvX + 3, relvY + 3, relvW - 6, relvH - 6, 3, 3); // Linhas fora
                g2d.drawLine(w/2, relvY + 3, w/2, relvY + relvH - 3); // Linha de meio campo vertical
                g2d.drawOval(w/2 - 15, h/2 - 15, 30, 30); // Círculo central

                // Pequenas áreas de penálti
                g2d.drawRect(relvX + 3, h/2 - 20, 25, 40); // Baliza Oeste
                g2d.drawRect(relvX + relvW - 28, h/2 - 20, 25, 40); // Baliza Este
            }
        };
        painelGraficoEstadio.setOpaque(false);
        painelGraficoEstadio.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // LÓGICA DE CLIQUE PARA CALCULAR CAPACIDADES
        painelGraficoEstadio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cx = painelGraficoEstadio.getWidth() / 2;
                int cy = painelGraficoEstadio.getHeight() / 2;

                // Distância do clique ao centro (para ignorar cliques no relvado)
                double dist = Math.sqrt(Math.pow(e.getX() - cx, 2) + Math.pow(e.getY() - cy, 2));
                if (dist < 40) return; // Clicou no relvado, ignora

                // Calcular ângulo do clique
                // Invertemos o Y porque no ecrã o Y cresce para baixo, e a matemática normal é para cima
                double dx = e.getX() - cx;
                double dy = cy - e.getY();
                double angle = Math.toDegrees(Math.atan2(dy, dx));
                if (angle < 0) angle += 360;

                int capTotal = estadio.getCapacidade();
                int cap30 = (int) (capTotal * 0.30); // 30%
                int cap20 = (int) (capTotal * 0.20); // 20%

                // Determinar a zona baseada no ângulo
                if (angle >= 45 && angle < 135) {
                    lblInfoClicada.setText("ZONA NORTE: " + String.format("%,d", cap30) + " lugares (30%)");
                    lblInfoClicada.setForeground(COR_NORTE);
                } else if (angle >= 135 && angle < 225) {
                    lblInfoClicada.setText("ZONA OESTE: " + String.format("%,d", cap20) + " lugares (20%)");
                    lblInfoClicada.setForeground(COR_OESTE);
                } else if (angle >= 225 && angle < 315) {
                    lblInfoClicada.setText("ZONA SUL: " + String.format("%,d", cap30) + " lugares (30%)");
                    lblInfoClicada.setForeground(COR_SUL);
                } else {
                    lblInfoClicada.setText("ZONA ESTE: " + String.format("%,d", cap20) + " lugares (20%)");
                    lblInfoClicada.setForeground(COR_ESTE);
                }
            }
        });

        painelDireita.add(painelGraficoEstadio, BorderLayout.CENTER);

        // Legenda e Info Dinâmica no fundo
        JPanel pnlSulDireita = new JPanel(new BorderLayout());
        pnlSulDireita.setOpaque(false);

        JPanel pnlLegenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlLegenda.setOpaque(false);
        pnlLegenda.add(criarItemLegenda("Norte (30%)", COR_NORTE));
        pnlLegenda.add(criarItemLegenda("Sul (30%)", COR_SUL));
        pnlLegenda.add(criarItemLegenda("Este (20%)", COR_ESTE));
        pnlLegenda.add(criarItemLegenda("Oeste (20%)", COR_OESTE));

        pnlSulDireita.add(pnlLegenda, BorderLayout.NORTH);
        pnlSulDireita.add(lblInfoClicada, BorderLayout.SOUTH);

        painelDireita.add(pnlSulDireita, BorderLayout.SOUTH);

        painelCentro.add(painelDireita);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
    }

    // ==========================================
    // MÉTODOS DE DESIGN UI
    // ==========================================

    private JPanel criarCartaoModerno(String titulo, String valor, Color corSotaque) {
        RoundedPanel cartao = new RoundedPanel(15, COR_CARTAO);
        cartao.setLayout(new BorderLayout(15, 0));

        // Barra colorida lateral
        JPanel barraEsq = new JPanel();
        barraEsq.setBackground(corSotaque);
        barraEsq.setPreferredSize(new Dimension(6, 0)); // Largura da barra
        cartao.add(barraEsq, BorderLayout.WEST);

        // Textos
        JPanel pnlTextos = new JPanel(new GridLayout(2, 1));
        pnlTextos.setOpaque(false);
        pnlTextos.setBorder(new EmptyBorder(12, 10, 12, 10)); // Margens internas

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COR_TEXTO_SECUNDARIO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));

        pnlTextos.add(lblTitulo);
        pnlTextos.add(lblValor);
        cartao.add(pnlTextos, BorderLayout.CENTER);

        return cartao;
    }

    private JPanel criarItemLegenda(String texto, Color cor) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnl.setOpaque(false);

        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cor);
                g2.fillOval(0, 0, 10, 10);
            }
        };
        circulo.setPreferredSize(new Dimension(10, 10));
        circulo.setOpaque(false);

        JLabel lbl = new JLabel(texto);
        lbl.setForeground(COR_TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pnl.add(circulo);
        pnl.add(lbl);
        return pnl;
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