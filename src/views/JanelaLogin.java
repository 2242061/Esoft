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

        btnLogin.addActionListener(e -> {
            String emailInserido = txtUsername.getText();
            String passwordInserida = new String(txtPassword.getPassword());

            Utilizador utilizadorLogado = null;

            // Procura na base de dados
            for (Utilizador u : service.CentralDeDados.getInstance().getUtilizadores()) {
                if (u.autenticar(emailInserido, passwordInserida)) {
                    utilizadorLogado = u;
                    break;
                }
            }

            if (utilizadorLogado != null) {
                // Se a conta for de ADMIN, abre o Dashboard
                if (utilizadorLogado.isAdmin()) {
                    JFrame frameAtual = (JFrame) SwingUtilities.getWindowAncestor(painelPrincipal);
                    if (frameAtual != null) frameAtual.dispose();

                    JFrame frameMenu = new JFrame("Sistema Mundial 2026 - Dashboard");
                    frameMenu.setContentPane(new MenuPrincipal().getPainelPrincipal());
                    frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameMenu.setSize(1100, 700);
                    frameMenu.setLocationRelativeTo(null);
                    frameMenu.setVisible(true);
                }
                // Se for uma conta normal (USER), mostra a mensagem
                else {
                    JOptionPane.showMessageDialog(painelPrincipal, "Página cliente em progresso...", "Área de Cliente", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(painelPrincipal, "Email ou password incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão REGISTRE-SE
        registreSeButton.addActionListener(e -> {
            JTextField txtNome = new JTextField();
            JTextField txtEmail = new JTextField();
            JPasswordField txtPass = new JPasswordField();

            Object[] msg = {"Nome:", txtNome, "Email:", txtEmail, "Password:", txtPass};
            int opcao = JOptionPane.showConfirmDialog(painelPrincipal, msg, "Criar Conta", JOptionPane.OK_CANCEL_OPTION);

            if (opcao == JOptionPane.OK_OPTION) {
                try {
                    int novoId = service.CentralDeDados.getInstance().getUtilizadores().size() + 1;
                    String pass = new String(txtPass.getPassword());

                    Utilizador novo = new Utilizador(novoId, txtNome.getText(), txtEmail.getText(), pass, "USER");
                    service.CentralDeDados.getInstance().getUtilizadores().add(novo);

                    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("utilizadores.txt", true))) {
                        bw.write(novo.getId() + ";" + novo.getNome() + ";" + novo.getEmail() + ";" + novo.getPassword() + ";" + novo.getTipo());
                        bw.newLine();
                    }

                    JOptionPane.showMessageDialog(painelPrincipal, "Conta criada com sucesso! Já podes fazer login.");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(painelPrincipal, "Erro ao criar conta.");
                }
            }
        });
    }

    public JPanel getPainelPrincipal() {
        return painelPrincipal;
    }
}