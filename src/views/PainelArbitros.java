package views;

import model.Arbitro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PainelArbitros {
    private JPanel painelPrincipal;
    private JTable tabelaArbitros;
    private DefaultTableModel modeloTabela;

    private final Color COR_FUNDO = new Color(18, 19, 25);
    private final Color COR_CARTAO = new Color(28, 29, 38);
    private final Color COR_ROXO = new Color(114, 72, 232);

    public PainelArbitros() {
        painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. TOPO (Título)
        JLabel lblTitulo = new JLabel("Gestão de Árbitros", SwingConstants.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // 2. CENTRO (Tabela)
        String[] colunas = {"ID", "Nome", "Nacionalidade", "Anos Experiência"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // --- PREENCHER COM OS DADOS DA CENTRAL ---
        for (Arbitro a : service.CentralDeDados.getInstance().getArbitros()) {
            modeloTabela.addRow(new Object[]{
                    a.getId(),
                    a.getNome(),
                    a.getNacionalidade(),
                    a.getAnosExperiencia()
            });
        }
        // -----------------------------------------

        tabelaArbitros = new JTable(modeloTabela);
        estilizarTabela(tabelaArbitros);

        tabelaArbitros = new JTable(modeloTabela);
        estilizarTabela(tabelaArbitros);

        // --- ADICIONAR O DUPLO CLIQUE AQUI ---
        tabelaArbitros.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaArbitros.getSelectedRow() != -1) {
                    int row = tabelaArbitros.getSelectedRow();
                    int idArbitro = (int) modeloTabela.getValueAt(row, 0);

                    // Buscar o árbitro na CentralDeDados
                    model.Arbitro arbitroSelecionado = null;
                    for (model.Arbitro a : service.CentralDeDados.getInstance().getArbitros()) {
                        if (a.getId() == idArbitro) {
                            arbitroSelecionado = a;
                            break;
                        }
                    }

                    if (arbitroSelecionado != null) {
                        // Navegar para o Perfil
                        JPanel parent = (JPanel) painelPrincipal.getParent();
                        parent.removeAll();
                        parent.add(new PainelPerfilArbitro(arbitroSelecionado).getPainelPrincipal(), BorderLayout.CENTER);
                        parent.revalidate();
                        parent.repaint();
                    }
                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(tabelaArbitros);
        scrollPane.getViewport().setBackground(COR_CARTAO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_CARTAO));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // 3. BASE (Botões)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        painelBotoes.setBackground(COR_FUNDO);

        JButton btnAdicionar = criarBotaoPersonalizado("Adicionar Árbitro", COR_ROXO, Color.WHITE);
        btnAdicionar.addActionListener(e -> abrirDialogoAdicionar());
        JButton btnRemover = criarBotaoPersonalizado("Remover", new Color(200, 50, 50), Color.WHITE);
        btnRemover.addActionListener(e -> removerArbitroSelecionado());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        // TODO: Carregar dados da CentralDeDados aqui no futuro
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
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID centrado
        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Experiência centrada
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
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }
    private void abrirDialogoAdicionar() {
        JTextField txtNome = new JTextField();
        JTextField txtNacionalidade = new JTextField();
        JTextField txtExperiencia = new JTextField();

        Object[] mensagem = {
                "Nome do Árbitro:", txtNome,
                "País/Nacionalidade:", txtNacionalidade,
                "Anos de Experiência:", txtExperiencia
        };

        // Mostra o pop-up
        int opcao = JOptionPane.showConfirmDialog(painelPrincipal, mensagem, "Adicionar Novo Árbitro", JOptionPane.OK_CANCEL_OPTION);

        if (opcao == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText().trim();
                String nacionalidade = txtNacionalidade.getText().trim();
                int experiencia = Integer.parseInt(txtExperiencia.getText().trim());

                if (nome.isEmpty() || nacionalidade.isEmpty()) {
                    JOptionPane.showMessageDialog(painelPrincipal, "Erro: O nome e a nacionalidade são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 1. Descobrir qual é o próximo ID disponível
                int novoId = 1;
                for (model.Arbitro a : service.CentralDeDados.getInstance().getArbitros()) {
                    if (a.getId() >= novoId) {
                        novoId = a.getId() + 1;
                    }
                }

                // 2. Criar o objeto
                model.Arbitro novoArbitro = new model.Arbitro(novoId, nome, nacionalidade, experiencia);

                // 3. Guardar na memória (CentralDeDados)
                service.CentralDeDados.getInstance().getArbitros().add(novoArbitro);

                // 4. Adicionar à Tabela visualmente
                modeloTabela.addRow(new Object[]{
                        novoArbitro.getId(),
                        novoArbitro.getNome(),
                        novoArbitro.getNacionalidade(),
                        novoArbitro.getAnosExperiencia()
                });

                // 5. Guardar permanentemente no ficheiro .txt
                guardarArbitroNoFicheiro(novoArbitro);

                JOptionPane.showMessageDialog(painelPrincipal, "Árbitro adicionado com sucesso!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(painelPrincipal, "Erro: Os anos de experiência devem ser um número inteiro!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painelPrincipal, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArbitroNoFicheiro(model.Arbitro a) {
        // O "true" no FileWriter serve para adicionar ao final do ficheiro sem apagar o que lá está
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("arbitros.txt", true))) {
            bw.write(a.getId() + ";" + a.getNome() + ";" + a.getNacionalidade() + ";" + a.getAnosExperiencia());
            bw.newLine();
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(painelPrincipal, "Erro ao guardar no ficheiro de texto!", "Erro de Ficheiro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerArbitroSelecionado() {
        // 1. Verificar se o utilizador clicou nalguma linha da tabela
        int linhaSelecionada = tabelaArbitros.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(painelPrincipal, "Por favor, selecione um árbitro na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Ir buscar o ID e o Nome (para a mensagem) à tabela visual
        int idArbitro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String nomeArbitro = (String) modeloTabela.getValueAt(linhaSelecionada, 1);

        // 3. Confirmar se ele tem mesmo a certeza (prevenir acidentes)
        int confirmacao = JOptionPane.showConfirmDialog(painelPrincipal,
                "Tem a certeza que deseja remover o árbitro '" + nomeArbitro + "'?",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            // 4. Remover da memória (CentralDeDados)
            service.CentralDeDados.getInstance().getArbitros().removeIf(a -> a.getId() == idArbitro);

            // 5. Remover da tabela visual
            modeloTabela.removeRow(linhaSelecionada);

            // 6. Atualizar o ficheiro de texto
            reescreverFicheiroArbitros();

            JOptionPane.showMessageDialog(painelPrincipal, "Árbitro removido com sucesso!");
        }
    }

    private void reescreverFicheiroArbitros() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("arbitros.txt", false))) {

            for (model.Arbitro a : service.CentralDeDados.getInstance().getArbitros()) {
                bw.write(a.getId() + ";" + a.getNome() + ";" + a.getNacionalidade() + ";" + a.getAnosExperiencia());
                bw.newLine();
            }

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(painelPrincipal, "Erro ao atualizar o ficheiro de texto!", "Erro de Ficheiro", JOptionPane.ERROR_MESSAGE);
        }
    }


    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}