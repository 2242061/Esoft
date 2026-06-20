package views;

import model.Equipa;
import model.Jogador;
import service.CentralDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PainelEquipaPerfil {
    private JPanel painelPrincipal;
    private Equipa equipaAtual;
    private DefaultTableModel modeloTabela; // Transformado em variável global para podermos atualizar

    public PainelEquipaPerfil(Equipa equipa) {
        this.equipaAtual = equipa;

        if (painelPrincipal == null) {
            painelPrincipal = new JPanel();
        }
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(new Color(25, 26, 28));

        // 1. CABEÇALHO COM BOTÃO VOLTAR
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(25, 26, 28));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton btnVoltar = new JButton("<< Voltar aos Grupos");
        btnVoltar.setBackground(new Color(240, 240, 240));
        btnVoltar.setForeground(new Color(25, 26, 28));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel painelConteudo = (JPanel) painelPrincipal.getParent();
                painelConteudo.removeAll();
                painelConteudo.add(new PainelGrupos().getPainelPrincipal(), BorderLayout.CENTER);
                painelConteudo.revalidate();
                painelConteudo.repaint();
            }
        });

        JLabel lblNomeEquipa = new JLabel(equipa.getNome().toUpperCase(), SwingConstants.CENTER);
        lblNomeEquipa.setForeground(Color.WHITE);
        lblNomeEquipa.setFont(new Font("Arial", Font.BOLD, 34));

        painelTopo.add(btnVoltar, BorderLayout.WEST);
        painelTopo.add(lblNomeEquipa, BorderLayout.CENTER);

        JLabel lblEspaco = new JLabel("");
        lblEspaco.setPreferredSize(new Dimension(200, 40));
        painelTopo.add(lblEspaco, BorderLayout.EAST);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // 2. CORPO DO PERFIL
        JPanel painelCentro = new JPanel(new BorderLayout(20, 20));
        painelCentro.setBackground(new Color(25, 26, 28));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));

        JPanel painelInfo = new JPanel(new GridLayout(1, 2, 20, 0));
        painelInfo.setBackground(new Color(35, 36, 38));
        painelInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 62, 65), 1, true),
                "Detalhes da Seleção"
        ));
        ((javax.swing.border.TitledBorder)painelInfo.getBorder()).setTitleColor(Color.LIGHT_GRAY);
        ((javax.swing.border.TitledBorder)painelInfo.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblPais = new JLabel("  País Representado: " + equipa.getPais());
        lblPais.setForeground(Color.WHITE);
        lblPais.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblGrupo = new JLabel("  Grupo de Qualificação: Grupo " + equipa.getGrupo());
        lblGrupo.setForeground(Color.WHITE);
        lblGrupo.setFont(new Font("Arial", Font.PLAIN, 16));

        painelInfo.add(lblPais);
        painelInfo.add(lblGrupo);
        painelInfo.setPreferredSize(new Dimension(0, 80));

        painelCentro.add(painelInfo, BorderLayout.NORTH);

        // 3. TABELA DE CONVOCATÓRIA
        JPanel painelPlantel = new JPanel(new BorderLayout());
        painelPlantel.setBackground(new Color(35, 36, 38));
        painelPlantel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 62, 65), 1, true),
                "Convocatória (Plantel)"
        ));
        ((javax.swing.border.TitledBorder)painelPlantel.getBorder()).setTitleColor(Color.LIGHT_GRAY);
        ((javax.swing.border.TitledBorder)painelPlantel.getBorder()).setTitleFont(new Font("Arial", Font.BOLD, 14));

        String[] colunas = {"ID", "Nome do Jogador", "Posição", "Clube", "Rating"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaJogadores = new JTable(modeloTabela);
        tabelaJogadores.setBackground(new Color(35, 36, 38));
        tabelaJogadores.setForeground(Color.WHITE);
        tabelaJogadores.setRowHeight(30);
        tabelaJogadores.getTableHeader().setBackground(new Color(25, 26, 28));
        tabelaJogadores.getTableHeader().setForeground(Color.LIGHT_GRAY);
        tabelaJogadores.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaJogadores.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaJogadores.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tabelaJogadores.getColumnModel().getColumn(0).setMaxWidth(50);
        tabelaJogadores.getColumnModel().getColumn(4).setMaxWidth(80);

        tabelaJogadores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tabelaJogadores.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linhaClicada = tabelaJogadores.getSelectedRow();
                    if (linhaClicada != -1 && !tabelaJogadores.getValueAt(linhaClicada, 0).equals("-")) {
                        int idJogador = (int) tabelaJogadores.getValueAt(linhaClicada, 0);

                        // 1. Procurar o jogador completo
                        CentralDeDados bd = CentralDeDados.getInstance();
                        Jogador jogadorSelecionado = null;
                        for (Jogador j : bd.getJogadores()) {
                            if (j.getId() == idJogador) {
                                jogadorSelecionado = j;
                                break;
                            }
                        }
                        if (jogadorSelecionado != null) {
                            JPanel painelConteudo = (JPanel) painelPrincipal.getParent();
                            painelConteudo.removeAll();
                            painelConteudo.add(new PainelJogadorPerfil(jogadorSelecionado, equipaAtual).getPainelPrincipal(), BorderLayout.CENTER);
                            painelConteudo.revalidate();
                            painelConteudo.repaint();
                        }
                    }
                }
            }
        });

        JScrollPane scrollPlantel = new JScrollPane(tabelaJogadores);
        scrollPlantel.setBorder(BorderFactory.createEmptyBorder());
        scrollPlantel.getViewport().setBackground(new Color(35, 36, 38));
        painelPlantel.add(scrollPlantel, BorderLayout.CENTER);

        JPanel painelAcoesPlantel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAcoesPlantel.setBackground(new Color(35, 36, 38));

        JButton btnAdicionarJogador = new JButton("+ Adicionar Convocado");
        btnAdicionarJogador.setVisible(CentralDeDados.getInstance().isAdmin());
        btnAdicionarJogador.setBackground(new Color(240, 240, 240));
        btnAdicionarJogador.setForeground(new Color(25, 26, 28));
        btnAdicionarJogador.setFont(new Font("Arial", Font.BOLD, 12));
        btnAdicionarJogador.setFocusPainted(false);
        btnAdicionarJogador.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAdicionarJogador.addActionListener(e -> abrirFormularioNovoJogador());

        painelAcoesPlantel.add(btnAdicionarJogador);
        painelPlantel.add(painelAcoesPlantel, BorderLayout.SOUTH);

        painelCentro.add(painelPlantel, BorderLayout.CENTER);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        // Preenche a tabela pela primeira vez
        atualizarTabelaJogadores();
    }

    // MÉTODO QUE ATUALIZA A TABELA COM OS JOGADORES REAIS
    private void atualizarTabelaJogadores() {
        modeloTabela.setRowCount(0); // Limpa as linhas todas

        CentralDeDados bd = CentralDeDados.getInstance();
        boolean temJogadores = false;

        for (Jogador j : bd.getJogadores()) {
            if (j.getPais().equalsIgnoreCase(equipaAtual.getNome())) {
                modeloTabela.addRow(new Object[]{j.getId(), j.getNome(), j.getPosicao(), j.getClube(), j.getRatingGeral()});
                temJogadores = true;
            }
        }

        if (!temJogadores) {
            modeloTabela.addRow(new Object[]{"-", "Nenhum jogador registado", "-", "-", "-"});
        }
    }

    // O FORMULÁRIO (POP-UP) PARA CRIAR O JOGADOR
    private void abrirFormularioNovoJogador() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(painelPrincipal), "Novo Jogador", true);
        dialog.setSize(400, 400); // Aumentei ligeiramente o tamanho
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 linhas agora
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ... (os campos que já tinhas) ...
        JTextField txtNome = new JTextField(); form.add(new JLabel("Nome:")); form.add(txtNome);
        JTextField txtClube = new JTextField(); form.add(new JLabel("Clube:")); form.add(txtClube);
        JComboBox<String> cbPosicao = new JComboBox<>(new String[]{"Guarda-Redes", "Defesa", "Médio", "Avançado"});
        form.add(new JLabel("Posição:")); form.add(cbPosicao);

        JTextField txtAltura = new JTextField(); form.add(new JLabel("Altura:")); form.add(txtAltura);
        JTextField txtPeso = new JTextField(); form.add(new JLabel("Peso:")); form.add(txtPeso);
        JTextField txtRating = new JTextField(); form.add(new JLabel("Rating:")); form.add(txtRating);

        // NOVO CAMPO: Camisola
        JTextField txtCamisola = new JTextField();
        form.add(new JLabel("Camisola:"));
        form.add(txtCamisola);

        dialog.add(form, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                // ... (os teus parses de altura, peso, rating) ...
                int camisola = Integer.parseInt(txtCamisola.getText()); // Lê a camisola

                CentralDeDados bd = CentralDeDados.getInstance();
                int novoId = bd.getJogadores().size() + 1;

                // AGORA PASSAS OS 9 PARÂMETROS
                Jogador novoJogador = new Jogador(
                        novoId,
                        txtNome.getText(),
                        equipaAtual.getNome(),
                        txtClube.getText(),
                        (String) cbPosicao.getSelectedItem(),
                        Double.parseDouble(txtAltura.getText().replace(",", ".")),
                        Double.parseDouble(txtPeso.getText().replace(",", ".")),
                        Integer.parseInt(txtRating.getText()),
                        camisola // 9º Parâmetro (Corrigido!)
                );

                bd.getJogadores().add(novoJogador);
                bd.guardarTodosJogadoresNoFicheiro();
                dialog.dispose();
                atualizarTabelaJogadores();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro nos dados. Verifica se inseriste números nos campos corretos.");
            }
        });
        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    // MÉTODO QUE DESENHA O CARTÃO DO JOGADOR
    private void abrirCartaoJogador(int idJogador) {
        // 1. Encontrar o jogador na Base de Dados
        CentralDeDados bd = CentralDeDados.getInstance();
        Jogador jogadorSelecionado = null;
        for (Jogador j : bd.getJogadores()) {
            if (j.getId() == idJogador) {
                jogadorSelecionado = j;
                break;
            }
        }

        if (jogadorSelecionado == null) return;

        // 2. Criar a janela do Cartão
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(painelPrincipal), "Perfil de Atleta", true);
        dialog.setSize(350, 450);
        dialog.setLocationRelativeTo(painelPrincipal);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(25, 26, 28));

        // 3. Cabeçalho do Cartão (Nome e Rating)
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(new Color(35, 36, 38));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblRating = new JLabel(String.valueOf(jogadorSelecionado.getRatingGeral()), SwingConstants.CENTER);
        lblRating.setFont(new Font("Arial", Font.BOLD, 48));
        // Cor do rating muda consoante a qualidade do jogador!
        if (jogadorSelecionado.getRatingGeral() >= 80) lblRating.setForeground(new Color(255, 204, 0)); // Ouro
        else if (jogadorSelecionado.getRatingGeral() >= 75) lblRating.setForeground(new Color(192, 192, 192)); // Prata
        else lblRating.setForeground(new Color(205, 127, 50)); // Bronze

        JLabel lblNome = new JLabel(jogadorSelecionado.getNome().toUpperCase(), SwingConstants.CENTER);
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Arial", Font.BOLD, 18));

        painelTopo.add(lblRating, BorderLayout.NORTH);
        painelTopo.add(lblNome, BorderLayout.CENTER);
        dialog.add(painelTopo, BorderLayout.NORTH);

        // 4. Detalhes (Posição, Clube, Altura, Peso)
        JPanel painelDetalhes = new JPanel(new GridLayout(5, 1, 5, 5));
        painelDetalhes.setBackground(new Color(25, 26, 28));
        painelDetalhes.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        painelDetalhes.add(criarLinhaDetalhe("Posição:", jogadorSelecionado.getPosicao()));
        painelDetalhes.add(criarLinhaDetalhe("Clube Atual:", jogadorSelecionado.getClube()));
        painelDetalhes.add(criarLinhaDetalhe("País:", jogadorSelecionado.getPais()));
        painelDetalhes.add(criarLinhaDetalhe("Altura:", jogadorSelecionado.getAltura() + " m"));
        painelDetalhes.add(criarLinhaDetalhe("Peso:", jogadorSelecionado.getPeso() + " kg"));

        dialog.add(painelDetalhes, BorderLayout.CENTER);

        // 5. Botão de Fechar
        JPanel painelFundo = new JPanel();
        painelFundo.setBackground(new Color(25, 26, 28));
        JButton btnFechar = new JButton("Fechar Cartão");
        btnFechar.setBackground(new Color(60, 62, 65));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFechar.addActionListener(e -> dialog.dispose());

        painelFundo.add(btnFechar);
        dialog.add(painelFundo, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Método auxiliar para criar as linhas de texto bonitas no cartão
    private JPanel criarLinhaDetalhe(String etiqueta, String valor) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBackground(new Color(25, 26, 28));

        JLabel lblEsq = new JLabel(etiqueta);
        lblEsq.setForeground(Color.LIGHT_GRAY);
        lblEsq.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblDir = new JLabel(valor);
        lblDir.setForeground(Color.WHITE);
        lblDir.setFont(new Font("Arial", Font.BOLD, 14));

        linha.add(lblEsq, BorderLayout.WEST);
        linha.add(lblDir, BorderLayout.EAST);

        // Adicionar uma linhazinha subtil de separação no fundo
        linha.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 52, 55)));

        return linha;
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}