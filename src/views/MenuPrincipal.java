package views;

import service.CentralDeDados;
import model.Estadio;
import model.Jogador;
import model.Jogo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal {
    private JPanel painelPrincipal;
    private JPanel painelMenuLateral;
    private JPanel painelConteudo;

    // Nomes atualizados para bater certo com as Labels que acabaste de criar
    private JLabel lblDataVendas;
    private JLabel lblVendasTotais;
    private JLabel lblProximoJogo;
    private JLabel lblMelhorMarcador;
    private JLabel lblMaiorEstadio;

    private JButton gruposButton;
    private JButton alojamentoButton;
    private JButton calendarioButton;
    private JButton bilhetesButton;
    private JButton estadiosButton;
    private JButton arbitrosButton;
    private JButton estatisticasButton;

    public MenuPrincipal() {
        // 1. Ir buscar a nossa Base de Dados central
        CentralDeDados bd = CentralDeDados.getInstance();

        // 2. Estatísticas Reais! (Exemplo: ir buscar o primeiro jogo da lista, se existir)
        if (!bd.getJogos().isEmpty()) {
            Jogo proximoJogo = bd.getJogos().get(0);
            if (lblProximoJogo != null) {
                // Aqui usamos os nomes corretos da tua classe Jogo!
                lblProximoJogo.setText(proximoJogo.getEquipaCasa().getNome() + " vs " + proximoJogo.getEquipaFora().getNome());
            }
        } else {
            if (lblProximoJogo != null) lblProximoJogo.setText("Sem jogos agendados");
        }

        // Exemplo: Mostrar o Maior Estádio (aqui apanhamos apenas o primeiro para simplificar)
        if (!bd.getEstadios().isEmpty()) {
            Estadio estadio = bd.getEstadios().get(0);
            if (lblMaiorEstadio != null) {
                lblMaiorEstadio.setText(estadio.getNome() + " (" + estadio.getCapacidade() + ")");
            }
        }

        // Exemplo: Mostrar o melhor marcador
        if (!bd.getJogadores().isEmpty()) {
            Jogador melhor = bd.getJogadores().get(0);
            if (lblMelhorMarcador != null) {
                lblMelhorMarcador.setText(melhor.getNome() + " (" + melhor.getPais() + ")");
            }
        }

        // 3. Preencher os outros campos textuais
        if (lblDataVendas != null) lblDataVendas.setText("Estatísticas Atualizadas");
        if (lblVendasTotais != null) lblVendasTotais.setText("Total Equipas Registadas: " + bd.getEquipas().size());
        // Ligar o botão GRUPOS
        gruposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Limpar tudo o que está no painel da direita (ex: o Dashboard)
                painelConteudo.removeAll();

                // 2. Criar a nova página de Grupos e adicioná-la
                PainelGrupos paginaGrupos = new PainelGrupos();
                painelConteudo.add(paginaGrupos.getPainelPrincipal(), BorderLayout.CENTER);

                // 3. Atualizar o ecrã para forçar o Java a mostrar as mudanças
                painelConteudo.revalidate();
                painelConteudo.repaint();
            }
        });
        // Supondo que 'painelConteudo' é o painel central onde carregas os outros ecrãs
        estadiosButton.addActionListener(e -> {
            // 1. Limpa o que está atualmente no centro
            painelConteudo.removeAll();

            // 2. Adiciona o painel de estádios
            painelConteudo.add(new PainelEstadios().getPainelPrincipal(), BorderLayout.CENTER);

            // 3. Atualiza a interface
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        // Ligar o botão CALENDÁRIO
        calendarioButton.addActionListener(e -> {
            // 1. Limpa o que está atualmente no centro
            painelConteudo.removeAll();

            // 2. Adiciona o painel do calendário
            painelConteudo.add(new PainelCalendario().getPainelPrincipal(), BorderLayout.CENTER);

            // 3. Atualiza a interface
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
        // Ação do botão Arbitragem no Menu Principal
        arbitrosButton.addActionListener(e -> {

            painelConteudo.removeAll();
            painelConteudo.add(new PainelArbitros().getPainelPrincipal(), BorderLayout.CENTER);
            painelConteudo.revalidate();
            painelConteudo.repaint();
        });
    }


    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}