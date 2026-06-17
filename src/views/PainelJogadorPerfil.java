package views;

import model.Equipa;
import model.EstatisticaJogador;
import model.Jogador;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PainelJogadorPerfil {
    private JPanel painelPrincipal;
    private Jogador jogador;
    private Equipa equipaOrigem; // Para sabermos para onde voltar

    public PainelJogadorPerfil(Jogador jogador, Equipa equipaOrigem) {
        this.jogador = jogador;
        this.equipaOrigem = equipaOrigem;

        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(new Color(230, 230, 230)); // Fundo cinza claro como no Figma
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // ==========================================
        // 1. TOPO: FOTO E INFORMAÇÕES PESSOAIS
        // ==========================================
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        painelTopo.setBackground(new Color(230, 230, 230));

        // Quadrado da Foto
        JLabel lblFoto = new JLabel("Foto", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(180, 200));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(128, 128, 128));
        lblFoto.setForeground(Color.WHITE);
        lblFoto.setFont(new Font("Arial", Font.PLAIN, 24));

        // Texto com as informações
        JPanel painelInfo = new JPanel(new GridLayout(4, 1, 0, 10));
        painelInfo.setBackground(new Color(230, 230, 230));

        painelInfo.add(criarLabelFigma("Nome : " + jogador.getNome()));
        painelInfo.add(criarLabelFigma("País : " + jogador.getPais()));
        painelInfo.add(criarLabelFigma("Clube : " + jogador.getClube()));
        painelInfo.add(criarLabelFigma("Posição : " + jogador.getPosicao() + " | Camisola: " +jogador.getNumeroCamisola()));

        painelTopo.add(lblFoto);
        painelTopo.add(painelInfo);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // ==========================================
        // 2. CENTRO: TABELA (ESQUERDA) E GRELHA (DIREITA)
        // ==========================================
        JPanel painelCentro = new JPanel(new GridLayout(1, 2, 30, 0));
        painelCentro.setBackground(new Color(230, 230, 230));

        // ESQUERDA: Tabela de Estatísticas Recentes
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(new Color(230, 230, 230));

        JLabel lblTituloTabela = new JLabel("Estatísticas Recentes");
        lblTituloTabela.setFont(new Font("Arial", Font.BOLD, 18));
        painelTabela.add(lblTituloTabela, BorderLayout.NORTH);

        String[] colunas = {"Adversário", "Defesas", "Golos", "Cartões", "Rating", "Data"};
        DefaultTableModel modeloEstatisticas = new DefaultTableModel(colunas, 0);

        JTable tabelaStats = new JTable(modeloEstatisticas);
        tabelaStats.setRowHeight(40);
        tabelaStats.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Estilo do Cabeçalho (Verde)
        JTableHeader header = tabelaStats.getTableHeader();
        header.setBackground(new Color(102, 255, 102));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Centralizar o conteúdo
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaStats.getColumnCount(); i++) {
            tabelaStats.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // CARREGAR DADOS DO FICHEIRO
        CentralDeDados bd = CentralDeDados.getInstance();
        boolean temEstatisticas = false;

        for (EstatisticaJogador e : bd.getEstatisticasDoJogador(jogador.getId())) {
// {"Adversário", "Defesas", "Golos", "Cartões", "Rating", "Data"}

            modeloEstatisticas.addRow(new Object[]{
                    e.getAdversario(), // Coluna 0
                    "-",               // Coluna 1 (Defesas)
                    e.getGolos(),      // Coluna 2
                    e.getCartoes(),    // Coluna 3
                    e.getRating(),     // Coluna 4
                    e.getData()        // Coluna 5
            });
            temEstatisticas = true;
        }

        JScrollPane scroll = new JScrollPane(tabelaStats);
        painelTabela.add(scroll, BorderLayout.CENTER);
        painelCentro.add(painelTabela);

        // DIREITA: Grelha 2x2 de Estatísticas
        JPanel painelGrelhaStats = new JPanel(new GridLayout(2, 2, 10, 10));
        painelGrelhaStats.setBackground(new Color(230, 230, 230));
        painelGrelhaStats.add(criarCaixaStat("Advertências",
                bd.getTotalAmarelos(jogador.getId()) + " Cartões totais"));

        painelGrelhaStats.add(criarCaixaStat("Estatísticas Físicas",
                "Altura : " + jogador.getAltura() + "m\nPeso : " + jogador.getPeso() + " Kg"));

        painelGrelhaStats.add(criarCaixaStat("Estatísticas da carreira",
                "Jogos: " + bd.getTotalJogos(jogador.getId()) + " | Golos: " + bd.getTotalGolos(jogador.getId())));
        painelGrelhaStats.add(criarCaixaStat("Rating Geral", "Nacional : 8.7\nInternacional : " + (jogador.getRatingGeral() / 10.0)));

        painelCentro.add(painelGrelhaStats);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        // ==========================================
        // 3. FUNDO: BOTÃO BACK
        // ==========================================
        JPanel painelFundo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFundo.setBackground(new Color(230, 230, 230));

        JButton btnBack = new JButton("Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.BLACK);
        btnBack.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnBack.setPreferredSize(new Dimension(100, 35));
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            JPanel painelConteudo = (JPanel) painelPrincipal.getParent();
            painelConteudo.removeAll();
            painelConteudo.add(new PainelEquipaPerfil(equipaOrigem).getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });

        painelFundo.add(btnBack);
        painelPrincipal.add(painelFundo, BorderLayout.SOUTH);
        // Botão Editar (Junto ao Back)
        JButton btnEditar = new JButton("Editar Jogador");
        btnEditar.setBackground(new Color(240, 240, 240));
        btnEditar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnEditar.setPreferredSize(new Dimension(150, 35));

        btnEditar.addActionListener(e -> abrirFormularioEdicao());

        painelFundo.add(btnEditar);
        painelFundo.add(btnBack); // btnBack já existia
    }

    private void abrirFormularioEdicao() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(painelPrincipal), "Editar Jogador", true);
        dialog.setSize(400, 300); // Tamanho reduzido
        dialog.setLocationRelativeTo(painelPrincipal);
        dialog.setLayout(new BorderLayout());

        // Grelha simplificada para 4 campos
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Campos pré-preenchidos
        JTextField txtNome = new JTextField(jogador.getNome());
        JTextField txtClube = new JTextField(jogador.getClube());
        JTextField txtRating = new JTextField(String.valueOf(jogador.getRatingGeral()));
        JTextField txtCamisola = new JTextField(String.valueOf(jogador.getNumeroCamisola()));

        form.add(new JLabel("Nome:")); form.add(txtNome);
        form.add(new JLabel("Clube:")); form.add(txtClube);
        form.add(new JLabel("Rating:")); form.add(txtRating);
        form.add(new JLabel("Camisola:")); form.add(txtCamisola);

        dialog.add(form, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar Alterações");
        btnGuardar.addActionListener(e -> {
            try {
                // Atualizar apenas o que foi editado
                jogador.setNome(txtNome.getText());
                jogador.setClube(txtClube.getText());
                jogador.setRatingGeral(Integer.parseInt(txtRating.getText()));
                jogador.setNumeroCamisola(Integer.parseInt(txtCamisola.getText()));

                // Os outros campos (golos, etc.) continuam iguais ao que estavam

                // Gravar no ficheiro para persistir a alteração
                CentralDeDados.getInstance().guardarTodosJogadoresNoFicheiro();

                dialog.dispose();
                JOptionPane.showMessageDialog(painelPrincipal, "Jogador atualizado com sucesso!");

                // Recarregar a página para atualizar o visual
                JPanel parent = (JPanel) painelPrincipal.getParent();
                parent.removeAll();
                parent.add(new PainelJogadorPerfil(jogador, equipaOrigem).getPainelPrincipal());
                parent.revalidate();
                parent.repaint();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: Rating e Camisola devem ser números.");
            }
        });

        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    // Métodos Auxiliares de Design
    private JLabel criarLabelFigma(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.PLAIN, 22));
        lbl.setForeground(Color.BLACK);
        return lbl;
    }

    private JPanel criarCaixaStat(String titulo, String conteudo) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(210, 210, 210));
        painel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 4));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(180, 180, 180)));
        painel.add(lblTitulo, BorderLayout.NORTH);

        JTextArea txtConteudo = new JTextArea(conteudo);
        txtConteudo.setEditable(false);
        txtConteudo.setOpaque(false);
        txtConteudo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtConteudo.setMargin(new Insets(10, 10, 10, 10));
        painel.add(txtConteudo, BorderLayout.CENTER);

        return painel;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}