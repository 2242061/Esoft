package views;

import model.Utilizador; // IMPORTANTE: Importar o teu model!
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaLogin {
    private JPanel painelPrincipal;
    private JTextField txtUsername; // Este campo vai ser usado para o Email
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblTitulo;
    private JButton registreSeButton;
    private JLabel lblImagemTrofeu;

    public JanelaLogin() {
        // Carregar a imagem
        java.net.URL imgURL = getClass().getResource("/imagens/icone.png");

        if (imgURL != null) {
            lblImagemTrofeu.setIcon(new ImageIcon(imgURL));
        } else {
            System.out.println("ERRO: Não foi possível encontrar a imagem em /imagens/icone.png");
        }
        // Ação do botão ENTRAR
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Ir buscar o que o utilizador escreveu (Email e Password)
                String emailInserido = txtUsername.getText();
                String passwordInserida = new String(txtPassword.getPassword());

                // 2. Simular a nossa "Base de Dados" com o utilizador Admin
                Utilizador admin = new Utilizador(
                        1,
                        "Administrador",
                        "admin@mundial.pt",
                        "1234",
                        "ADMIN"
                );

                // 3. Usar a tua lógica de negócio para validar!
                if (admin.autenticar(emailInserido, passwordInserida)) {
                    // 2. Fechar a janela de Login atual
                    JFrame frameAtual = (JFrame) SwingUtilities.getWindowAncestor(painelPrincipal);
                    if (frameAtual != null) {
                        frameAtual.dispose(); // Fecha e limpa a janela da memória
                    }

                    // 3. Abrir o Menu Principal
                    JFrame frameMenu = new JFrame("Sistema Mundial 2026 - Dashboard");
                    frameMenu.setContentPane(new MenuPrincipal().getPainelPrincipal());
                    frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameMenu.setSize(1100, 700);
                    frameMenu.setResizable(false);
                    frameMenu.setLocationRelativeTo(null); // Centrar no ecrã
                    frameMenu.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(painelPrincipal, "Email ou password incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão REGISTRE-SE
        registreSeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(painelPrincipal, "Vamos abrir a janela de registo em breve!");
            }
        });
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}