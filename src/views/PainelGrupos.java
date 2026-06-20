package views;

import model.Equipa;
import model.Jogo;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PainelGrupos {
    private JPanel painelPrincipal;
    private JPanel painelGrelha;
    private JButton btnAnterior;
    private JButton btnProximo;

    private int paginaAtual = 0;
    private final int GRUPOS_POR_PAGINA = 6;
    private final String[] nomesGrupos = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};

    // Paleta de Cores Premium
    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);
    private final Color COR_TEXTO_SECUNDARIO = new Color(140, 140, 150);

    public PainelGrupos() {
        if (painelPrincipal == null) {
            painelPrincipal = new JPanel();
        }
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);

        // TOPO: Título
        JPanel painelTopoContainer = new JPanel(new BorderLayout());
        painelTopoContainer.setBackground(COR_FUNDO);
        painelTopoContainer.setBorder(new EmptyBorder(30, 40, 10, 40));

        JLabel lblTitulo = new JLabel("TABELA DE GRUPOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        painelTopoContainer.add(lblTitulo, BorderLayout.WEST);
        painelPrincipal.add(painelTopoContainer, BorderLayout.NORTH);

        // CENTRO: Grelha de Grupos
        painelGrelha = new JPanel(new GridLayout(0, 2, 25, 25)); // 2 colunas, espaçamento de 25px
        painelGrelha.setBackground(COR_FUNDO);
        painelGrelha.setBorder(new EmptyBorder(10, 40, 20, 40));

        JPanel painelGrelhaAlinhamento = new JPanel(new BorderLayout());
        painelGrelhaAlinhamento.setBackground(COR_FUNDO);
        painelGrelhaAlinhamento.add(painelGrelha, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(painelGrelhaAlinhamento);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(COR_FUNDO);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        // SUL: Paginação
        JPanel painelPaginacao = new JPanel(new BorderLayout());
        painelPaginacao.setBackground(COR_FUNDO);
        painelPaginacao.setBorder(new EmptyBorder(15, 50, 25, 50));

        btnAnterior = criarBotaoPaginacao("<- Página Anterior", COR_CARTAO, Color.WHITE);
        btnProximo = criarBotaoPaginacao("Próxima Página ->", COR_ROXO, Color.WHITE);

        painelPaginacao.add(btnAnterior, BorderLayout.WEST);
        painelPaginacao.add(btnProximo, BorderLayout.EAST);
        painelPrincipal.add(painelPaginacao, BorderLayout.SOUTH);

        // LÓGICA DE NAVEGAÇÃO
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paginaAtual > 0) {
                    paginaAtual--;
                    atualizarGrelha();
                }
            }
        });

        btnProximo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((paginaAtual + 1) * GRUPOS_POR_PAGINA < nomesGrupos.length) {
                    paginaAtual++;
                    atualizarGrelha();
                }
            }
        });

        atualizarGrelha();
    }

    private void atualizarGrelha() {
        painelGrelha.removeAll();

        // 1. Calcular a pontuação das equipas todas antes de desenhar a página
        Map<Equipa, EstatisticaEquipa> statsMap = calcularEstatisticas();

        int inicio = paginaAtual * GRUPOS_POR_PAGINA;
        int fim = Math.min(inicio + GRUPOS_POR_PAGINA, nomesGrupos.length);

        for (int i = inicio; i < fim; i++) {
            String letraGrupo = nomesGrupos[i];
            painelGrelha.add(criarCartaoGrupo(letraGrupo, statsMap));
        }

        btnAnterior.setVisible(paginaAtual > 0);
        btnProximo.setVisible(fim < nomesGrupos.length);

        painelGrelha.revalidate();
        painelGrelha.repaint();
    }

    // --- LÓGICA DE CÁLCULO DE PONTOS ---
    private Map<Equipa, EstatisticaEquipa> calcularEstatisticas() {
        Map<Equipa, EstatisticaEquipa> stats = new HashMap<>();

        // Inicializa todas a 0
        for (Equipa eq : CentralDeDados.getInstance().getEquipas()) {
            stats.put(eq, new EstatisticaEquipa(eq));
        }

        // Lê resultados guardados do Apito Final
        File ficheiroResultados = new File("resultados.txt");
        if (!ficheiroResultados.exists()) return stats;

        Map<Integer, int[]> resultadosLidos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiroResultados))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 3) {
                    int idJogo = Integer.parseInt(partes[0]);
                    int golosCasa = Integer.parseInt(partes[1]);
                    int golosFora = Integer.parseInt(partes[2]);
                    resultadosLidos.put(idJogo, new int[]{golosCasa, golosFora});
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler resultados: " + e.getMessage());
        }

        // Fazer a matemática e dar os pontos
        for (Jogo jogo : CentralDeDados.getInstance().getJogos()) {
            if (resultadosLidos.containsKey(jogo.getId())) {
                int golosCasa = resultadosLidos.get(jogo.getId())[0];
                int golosFora = resultadosLidos.get(jogo.getId())[1];

                EstatisticaEquipa statCasa = stats.get(jogo.getEquipaCasa());
                EstatisticaEquipa statFora = stats.get(jogo.getEquipaFora());

                if (statCasa == null || statFora == null) continue;

                // Processar Golos
                statCasa.jogos++; statCasa.golosMarcados += golosCasa; statCasa.golosSofridos += golosFora;
                statFora.jogos++; statFora.golosMarcados += golosFora; statFora.golosSofridos += golosCasa;

                // Processar Pontos (Vitória=3, Empate=1, Derrota=0)
                if (golosCasa > golosFora) {
                    statCasa.vitorias++; statCasa.pontos += 3;
                    statFora.derrotas++;
                } else if (golosCasa < golosFora) {
                    statFora.vitorias++; statFora.pontos += 3;
                    statCasa.derrotas++;
                } else {
                    statCasa.empates++; statCasa.pontos += 1;
                    statFora.empates++; statFora.pontos += 1;
                }
            }
        }
        return stats;
    }

    private JPanel criarCartaoGrupo(String nomeGrupo, Map<Equipa, EstatisticaEquipa> statsMap) {
        RoundedPanel cartao = new RoundedPanel(20, COR_CARTAO);
        cartao.setLayout(new BorderLayout());
        cartao.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblNomeGrupo = new JLabel("GRUPO " + nomeGrupo);
        lblNomeGrupo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNomeGrupo.setForeground(Color.WHITE);
        lblNomeGrupo.setBorder(new EmptyBorder(0, 5, 10, 0));
        cartao.add(lblNomeGrupo, BorderLayout.NORTH);

        // Filtrar e Ordenar equipas deste grupo usando o Collections.sort
        List<EstatisticaEquipa> equipasDoGrupo = new ArrayList<>();
        for (EstatisticaEquipa e : statsMap.values()) {
            if (e.equipa.getGrupo().equalsIgnoreCase(nomeGrupo)) {
                equipasDoGrupo.add(e);
            }
        }
        Collections.sort(equipasDoGrupo);

        String[] colunas = {"Seleção", "PTS", "J", "V", "E", "D", "DG"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Adicionar as linhas dinâmicas, com os valores reais!
        for (EstatisticaEquipa e : equipasDoGrupo) {
            modelo.addRow(new Object[]{
                    e.equipa.getNome(),
                    e.pontos,
                    e.jogos,
                    e.vitorias,
                    e.empates,
                    e.derrotas,
                    (e.golosMarcados - e.golosSofridos)
            });
        }

        if (equipasDoGrupo.isEmpty()) {
            modelo.addRow(new Object[]{"Sem equipas", "-", "-", "-", "-", "-", "-"});
        }

        JTable tabela = new JTable(modelo);
        tabela.setBackground(COR_CARTAO);
        tabela.setForeground(Color.WHITE);
        tabela.setRowHeight(35);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(COR_ROXO);
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(COR_FUNDO);
        header.setForeground(COR_TEXTO_SECUNDARIO);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 62, 65)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 10, 0, 0));

        for (int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
            if (i == 0) {
                tabela.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            } else {
                tabela.getColumnModel().getColumn(i).setPreferredWidth(35);
                tabela.getColumnModel().getColumn(i).setMinWidth(30);
                tabela.getColumnModel().getColumn(i).setMaxWidth(50);
                tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        tabela.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int linhaClicada = tabela.getSelectedRow();
                    if (linhaClicada != -1) {
                        String nomeEquipa = (String) tabela.getValueAt(linhaClicada, 0);

                        if (!nomeEquipa.equals("Sem equipas")) {
                            CentralDeDados bd = CentralDeDados.getInstance();
                            Equipa equipaSelecionada = null;
                            for (Equipa eq : bd.getEquipas()) {
                                if (eq.getNome().equals(nomeEquipa)) {
                                    equipaSelecionada = eq;
                                    break;
                                }
                            }

                            if (equipaSelecionada != null) {
                                JPanel painelConteudo = (JPanel) painelPrincipal.getParent();
                                painelConteudo.removeAll();
                                painelConteudo.add(new PainelEquipaPerfil(equipaSelecionada).getPainelPrincipal(), BorderLayout.CENTER);
                                painelConteudo.revalidate();
                                painelConteudo.repaint();
                            }
                        }
                    }
                }
            }
        });

        int alturaIdeal = (tabela.getRowCount() * tabela.getRowHeight()) + tabela.getTableHeader().getPreferredSize().height;
        tabela.setPreferredScrollableViewportSize(new Dimension(tabela.getPreferredSize().width, alturaIdeal));

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());
        scrollTabela.getViewport().setBackground(COR_CARTAO);
        scrollTabela.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollTabela.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cartao.add(scrollTabela, BorderLayout.CENTER);
        return cartao;
    }

    private JButton criarBotaoPaginacao(String texto, Color corFundo, Color corTexto) {
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
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }

    // --- CLASSE AUXILIAR PARA A MATEMÁTICA E ORDENAÇÃO ---
    class EstatisticaEquipa implements Comparable<EstatisticaEquipa> {
        Equipa equipa;
        int pontos = 0, jogos = 0, vitorias = 0, empates = 0, derrotas = 0;
        int golosMarcados = 0, golosSofridos = 0;

        public EstatisticaEquipa(Equipa equipa) {
            this.equipa = equipa;
        }

        @Override
        public int compareTo(EstatisticaEquipa outra) {
            if (this.pontos != outra.pontos) {
                return Integer.compare(outra.pontos, this.pontos);
            }
            int minhaDG = this.golosMarcados - this.golosSofridos;
            int outraDG = outra.golosMarcados - outra.golosSofridos;
            if (minhaDG != outraDG) {
                return Integer.compare(outraDG, minhaDG);
            }
            return Integer.compare(outra.golosMarcados, this.golosMarcados);
        }
    }


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