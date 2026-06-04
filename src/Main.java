import model.Alojamento;
import model.Arbitro;
import model.Bilhete;
import model.Deslocacao;
import model.Eliminatoria;
import model.Equipa;
import model.Estadio;
import model.Grupo;
import model.Jogador;
import model.Jogo;
import model.Utilizador;
import service.BilheteService;
import service.CampeonatoService;

public class Main {
    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("       SISTEMA MUNDIAL 2026");
        System.out.println("=====================================\n");

        // Criar utilizador admin
        Utilizador admin = new Utilizador(
                1,
                "Administrador",
                "admin@mundial.pt",
                "1234",
                "ADMIN"
        );

        System.out.println("Login admin:");
        if (admin.autenticar("admin@mundial.pt", "1234")) {
            System.out.println("Login realizado com sucesso.");
        } else {
            System.out.println("Dados de login inválidos.");
        }

        System.out.println();

        CampeonatoService campeonatoService = new CampeonatoService();
        BilheteService bilheteService = new BilheteService();

        // Criar equipas
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Equipa espanha = new Equipa(2, "Espanha", "Espanha", "A");
        Equipa brasil = new Equipa(3, "Brasil", "Brasil", "B");
        Equipa alemanha = new Equipa(4, "Alemanha", "Alemanha", "B");
        

        System.out.println("Equipas criadas:");
        System.out.println(portugal);
        System.out.println(espanha);
        System.out.println(brasil);
        System.out.println(alemanha);

        System.out.println();

        // Criar grupo
        Grupo grupoA = new Grupo("A");
        grupoA.adicionarEquipa(portugal);
        grupoA.adicionarEquipa(espanha);

        System.out.println("Grupo criado:");
        System.out.println(grupoA);

        System.out.println();

        // Criar estádio
        Estadio estadio = new Estadio(
                1,
                "Estádio de Alvalade",
                "Lisboa",
                50000
        );

        System.out.println("Estádio criado:");
        System.out.println(estadio);

        System.out.println();

        // Criar árbitro
        Arbitro arbitro = new Arbitro(
                1,
                "Artur Silva",
                "Portugal",
                10
        );

        System.out.println("Árbitro criado:");
        System.out.println(arbitro);

        System.out.println();

        // Criar jogo
        Jogo jogo = new Jogo(
                1,
                portugal,
                espanha,
                estadio,
                "20/05/2026",
                "20:30"
        );

        jogo.atribuirArbitro(arbitro);

        System.out.println("Jogo criado:");
        System.out.println(jogo.getInfoJogo());
        System.out.println("Árbitro: " + jogo.getArbitro().getNome());

        System.out.println();

        // Comprar bilhete
        Bilhete bilhete = new Bilhete(
                1,
                jogo,
                "João Costa",
                "123456789",
                "912345678",
                "A1",
                2,
                35.00
        );

        bilhete.vender();

        System.out.println("Compra de bilhete:");
        System.out.println(bilhete);
        System.out.println("Vendido: " + bilhete.isVendido());

        System.out.println();

        // Criar jogador
        Jogador jogador = new Jogador(
                1,
                "António Costa",
                "Portugal",
                "Sporting",
                "Defesa Esquerdo",
                1.88,
                83.0,
                87
        );

        System.out.println("Perfil do jogador:");
        System.out.println(jogador);

        System.out.println();

        // Criar deslocação
        Deslocacao deslocacao = new Deslocacao(
                1,
                portugal,
                "Porto",
                "Lisboa",
                "Autocarro",
                "19/05/2026",
                "10:00"
        );

        System.out.println("Deslocação:");
        System.out.println(deslocacao);

        System.out.println();

        // Criar alojamento
        Alojamento alojamento = new Alojamento(
                1,
                "Hotel Mundial",
                "Lisboa",
                "Avenida Central, Lisboa",
                30
        );

        alojamento.reservar();

        System.out.println("Alojamento:");
        System.out.println(alojamento);
        System.out.println("Reservado: " + alojamento.isReservado());

        System.out.println();

        // Registar resultado
        jogo.registarResultado(2, 1);

        System.out.println("Resultado do jogo:");
        System.out.println(jogo);

        if (jogo.obterVencedor() != null) {
            System.out.println("Vencedor: " + jogo.obterVencedor().getNome());
        } else {
            System.out.println("Empate ou jogo ainda não terminado.");
        }

        System.out.println();

        // Criar eliminatória
        Eliminatoria eliminatoria = new Eliminatoria(
                1,
                "Oitavos de Final",
                jogo
        );

        eliminatoria.definirVencedor(portugal);

        System.out.println("Eliminatória:");
        System.out.println(eliminatoria);

        System.out.println();

        System.out.println("=====================================");
        System.out.println("      TESTE DO SISTEMA TERMINADO");
        System.out.println("=====================================");
    }
}