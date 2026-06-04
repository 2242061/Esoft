package model;

public class Bilhete {
    private int id;
    private Jogo jogo;
    private String nomeComprador;
    private String nif;
    private String contacto;
    private String seccao;
    private int quantidade;
    private double precoUnitario;
    private boolean vendido;

    public Bilhete(int id, Jogo jogo, String nomeComprador, String nif, String contacto,
                   String seccao, int quantidade, double precoUnitario) {

        if (jogo == null) {
            throw new IllegalArgumentException("O jogo é obrigatório.");
        }

        if (nomeComprador == null || nomeComprador.isBlank()) {
            throw new IllegalArgumentException("O nome do comprador é obrigatório.");
        }

        if (nif == null || nif.length() != 9) {
            throw new IllegalArgumentException("O NIF deve ter 9 dígitos.");
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de bilhetes deve ser superior a zero.");
        }

        if (precoUnitario <= 0) {
            throw new IllegalArgumentException("O preço deve ser superior a zero.");
        }

        this.id = id;
        this.jogo = jogo;
        this.nomeComprador = nomeComprador;
        this.nif = nif;
        this.contacto = contacto;
        this.seccao = seccao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.vendido = false;
    }

    public void vender() {
        this.vendido = true;
    }

    public double calcularTotal() {
        return quantidade * precoUnitario;
    }

    public int getId() {
        return id;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public String getNif() {
        return nif;
    }

    public String getContacto() {
        return contacto;
    }

    public String getSeccao() {
        return seccao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public boolean isVendido() {
        return vendido;
    }

    @Override
    public String toString() {
        return "Bilhete #" + id +
                " | Comprador: " + nomeComprador +
                " | Jogo: " + jogo.getInfoJogo() +
                " | Secção: " + seccao +
                " | Total: " + calcularTotal() + "€";
    }
}