package model;

public class Utilizador {
    private int id;
    private String nome;
    private String email;
    private String password;
    private String tipo;

    public Utilizador(int id, String nome, String email, String password, String tipo) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email é obrigatório.");
        }

        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("A password deve ter pelo menos 4 caracteres.");
        }

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    public boolean autenticar(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public boolean isAdmin() {
        return tipo.equalsIgnoreCase("ADMIN");
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }
}