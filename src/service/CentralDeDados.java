package service;

import model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralDeDados {

    // ==========================================
    // 1. VARIÁVEIS E SINGLETON
    // ==========================================
    private static CentralDeDados instanciaUnica;


    private Utilizador utilizadorLogado; // Guarda quem fez login
    private List<Eliminatoria> listaEliminatorias = new ArrayList<>();
    private List<Equipa> equipas;
    private List<Estadio> estadios;
    private List<Jogo> jogos;
    private List<Jogador> jogadores;
    private List<EstatisticaJogador> estatisticas;
    private List<Arbitro> arbitros;
    private List<Utilizador> utilizadores;
    private List<Alojamento> alojamentos;


    private final String FICHEIRO_JOGADORES = "jogadores.txt";
    private final String FICHEIRO_JOGOS = "jogos.txt";
    private final String FICHEIRO_ESTADIOS = "estadios.txt";
    private final String FICHEIRO_ARBITROS = "arbitros.txt";
    private final String FICHEIRO_ESTATISTICAS = "estatisticas.txt";
    private final String FICHEIRO_FICHAS = "fichas_jogo.txt";
    private final String FICHEIRO_ALOJAMENTOS = "alojamento.txt";
    private final String FICHEIRO_UTILIZADORES = "utilizadores.txt";

    private CentralDeDados() {
        equipas = new ArrayList<>();
        estadios = new ArrayList<>();
        jogos = new ArrayList<>();
        jogadores = new ArrayList<>();
        estatisticas = new ArrayList<>();
        arbitros = new ArrayList<>();
        alojamentos = new ArrayList<>();
        utilizadores = new ArrayList<>();
        gerarEquipas();
        carregarEstadiosDoFicheiro();
        carregarAlojamentos();
        carregarJogadoresDoFicheiro();
        carregarEstatisticasDoFicheiro();
        carregarArbitros();
        carregarJogosDoFicheiro();
        carregarUtilizadores();
    }

    public static CentralDeDados getInstance() {
        if (instanciaUnica == null) {
            instanciaUnica = new CentralDeDados();
        }
        return instanciaUnica;
    }

    // ==========================================
    // 2. MÉTODOS DE LEITURA (LOADS)
    // ==========================================

    private void carregarJogadoresDoFicheiro() {
        File ficheiro = new File(FICHEIRO_JOGADORES);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                String[] partes = linha.split(";");
                if (partes.length >= 9) {
                    jogadores.add(new Jogador(
                            Integer.parseInt(partes[0]),  // id
                            partes[1],                    // nome
                            partes[2],                    // pais
                            partes[3],                    // clube
                            partes[4],                    // posicao
                            Double.parseDouble(partes[5]), // altura
                            Double.parseDouble(partes[6]), // peso
                            Integer.parseInt(partes[7]),  // rating
                            Integer.parseInt(partes[8])   // camisola
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar os jogadores: " + e.getMessage());
        }
    }

    private void carregarEstatisticasDoFicheiro() {
        File ficheiro = new File(FICHEIRO_ESTATISTICAS);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 8) {
                    int idJogador = Integer.parseInt(partes[1]);
                    Jogador jogador = null;

                    for (Jogador j : jogadores) {
                        if (j.getId() == idJogador) {
                            jogador = j;
                            break;
                        }
                    }

                    if (jogador != null) {
                        estatisticas.add(new EstatisticaJogador(
                                jogador,
                                partes[2], // Adversário
                                Integer.parseInt(partes[3]),   // Golos
                                Integer.parseInt(partes[4]),   // Assistências
                                Integer.parseInt(partes[5]),   // Cartões
                                Double.parseDouble(partes[6]), // Rating
                                partes[7]                      // Data
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar estatísticas: " + e.getMessage());
        }
    }

    private void carregarEstadiosDoFicheiro() {
        File ficheiro = new File(FICHEIRO_ESTADIOS);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 4) {
                    estadios.add(new Estadio(
                            Integer.parseInt(partes[0]),
                            partes[1],
                            partes[2],
                            Integer.parseInt(partes[3])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar estádios: " + e.getMessage());
        }
    }

    private void carregarJogosDoFicheiro() {
        File ficheiro = new File(FICHEIRO_JOGOS);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 6) {
                    int id = Integer.parseInt(partes[0]);

                    Equipa equipaCasa = null;
                    Equipa equipaFora = null;
                    for (Equipa eq : equipas) {
                        if (eq.getNome().equals(partes[1])) equipaCasa = eq;
                        if (eq.getNome().equals(partes[2])) equipaFora = eq;
                    }

                    Estadio estadio = null;
                    for (Estadio est : estadios) {
                        if (est.getNome().equals(partes[3])) estadio = est;
                    }

                    if (equipaCasa != null && equipaFora != null && estadio != null) {
                        jogos.add(new Jogo(id, equipaCasa, equipaFora, estadio, partes[4], partes[5]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jogos: " + e.getMessage());
        }
    }

    private void carregarArbitros() {
        File ficheiro = new File(FICHEIRO_ARBITROS);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    arbitros.add(new Arbitro(
                            Integer.parseInt(dados[0]),
                            dados[1],
                            dados[2],
                            Integer.parseInt(dados[3])
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar árbitros: " + e.getMessage());
        }
    }

    private void carregarAlojamentos() {
        File ficheiro = new File(FICHEIRO_ALOJAMENTOS);
        if (!ficheiro.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    alojamentos.add(new Alojamento(
                            Integer.parseInt(dados[0]),
                            dados[1],
                            dados[2],
                            dados[3],
                            Integer.parseInt(dados[4])
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar alojamentos: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. MÉTODOS DE ESCRITA (SAVES)
    // ==========================================

    public void guardarTodosJogadoresNoFicheiro() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHEIRO_JOGADORES))) {
            for (Jogador j : jogadores) {
                String linha = j.getId() + ";" + j.getNome() + ";" + j.getPais() + ";" +
                        j.getClube() + ";" + j.getPosicao() + ";" + j.getAltura() + ";" +
                        j.getPeso() + ";" + j.getRatingGeral() + ";" + j.getNumeroCamisola();
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao guardar os jogadores: " + e.getMessage());
        }
    }

    // ==========================================
    // 4. MÉTODOS DE CONSULTA (HELPERS)
    // ==========================================

    public int getTotalAmarelos(int id) {
        int total = 0;
        for (EstatisticaJogador e : estatisticas) {
            if (e.getJogador().getId() == id) total += e.getCartoes();
        }
        return total;
    }

    public int getTotalGolos(int id) {
        int total = 0;
        for (EstatisticaJogador e : estatisticas) {
            if (e.getJogador().getId() == id) total += e.getGolos();
        }
        return total;
    }

    public int getTotalJogos(int id) {
        int total = 0;
        for (EstatisticaJogador e : estatisticas) {
            if (e.getJogador().getId() == id) total++;
        }
        return total;
    }

    public List<EstatisticaJogador> getEstatisticasDoJogador(int jogadorId) {
        List<EstatisticaJogador> statsDoJogador = new ArrayList<>();
        for (EstatisticaJogador e : estatisticas) {
            if (e.getJogador().getId() == jogadorId) {
                statsDoJogador.add(e);
            }
        }
        return statsDoJogador;
    }

    public boolean existeFichaParaJogo(int idJogo) {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_FICHAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith(idJogo + ";")) return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public List<String> getJogadoresGuardadosNaFicha(int idJogo, int posicaoNaLinha) {
        List<String> jogadoresFicha = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_FICHAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length >= 4 && Integer.parseInt(partes[0]) == idJogo) {
                    String[] listaNomes = partes[posicaoNaLinha].split(",");
                    for (String nome : listaNomes) {
                        jogadoresFicha.add(nome.trim());
                    }
                    return jogadoresFicha;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler ficha do jogo: " + e.getMessage());
        }
        return jogadoresFicha;
    }

    // Retorna um array com [GolosCasa, GolosFora] ou null se o jogo ainda não se realizou
    public int[] getResultadoJogo(int idJogo) {
        java.io.File ficheiroResultados = new java.io.File("resultados.txt");
        if (!ficheiroResultados.exists()) return null;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(ficheiroResultados))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 3 && Integer.parseInt(partes[0]) == idJogo) {
                    return new int[]{Integer.parseInt(partes[1]), Integer.parseInt(partes[2])};
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar resultado: " + e.getMessage());
        }
        return null;
    }


    // ifnos base dashboard

    public Estadio getMaiorEstadio() {
        Estadio maior = null;
        for (Estadio e : estadios) {
            if (maior == null || e.getCapacidade() > maior.getCapacidade()) {
                maior = e;
            }
        }
        return maior;
    }

    public Jogador getMelhorMarcador() {
        Jogador melhor = null;
        int maxGolos = -1;
        for (Jogador j : jogadores) {
            int golos = getTotalGolos(j.getId());
            if (golos > maxGolos) {
                maxGolos = golos;
                melhor = j;
            }
        }
        return melhor;
    }
    public int getTotalAssistencias(int id) {
        int total = 0;
        for (EstatisticaJogador e : estatisticas) {
            if (e.getJogador().getId() == id) total += e.getAssistencias();
        }
        return total;
    }

    // Descobre o jogador com mais assistências
    public Jogador getMelhorAssistente() {
        Jogador melhor = null;
        int maxAssist = -1;
        for (Jogador j : jogadores) {
            int assist = getTotalAssistencias(j.getId());
            if (assist > maxAssist && assist > 0) { // Só conta se tiver pelo menos 1
                maxAssist = assist;
                melhor = j;
            }
        }
        return melhor;
    }

    // Descobre o jogador com mais amarelos
    public Jogador getJogadorMaisAgressivo() {
        Jogador pior = null;
        int maxAmarelos = -1;
        for (Jogador j : jogadores) {
            int amarelos = getTotalAmarelos(j.getId()); // Tu já tinhas este método!
            if (amarelos > maxAmarelos && amarelos > 0) {
                maxAmarelos = amarelos;
                pior = j;
            }
        }
        return pior;
    }

    public Jogo getProximoJogo() {
        for (Jogo j : jogos) {
            // Verifica se o resultado é nulo (ainda não aconteceu)
            if (getResultadoJogo(j.getId()) == null) {
                return j; // Retorna o primeiro que encontrar sem resultado
            }
        }
        return null; // Caso todos os jogos já tenham resultados (torneio terminado)
    }

    private void carregarUtilizadores() {
        java.io.File ficheiro = new java.io.File(FICHEIRO_UTILIZADORES);

        // Se não existir ou estiver vazio, cria o Admin e guarda no txt!
        if (!ficheiro.exists() || ficheiro.length() == 0) {
            Utilizador admin = new Utilizador(1, "Administrador", "admin@mundial.pt", "1234", "ADMIN");
            utilizadores.add(admin);

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(ficheiro))) {
                bw.write(admin.getId() + ";" + admin.getNome() + ";" + admin.getEmail() + ";" + admin.getPassword() + ";" + admin.getTipo());
                bw.newLine();
            } catch (Exception e) {
                System.out.println("Erro a criar ficheiro de utilizadores.");
            }
            return;
        }



        // Se já existir, lê os dados normalmente
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(ficheiro))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length == 5) {
                    utilizadores.add(new Utilizador(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar utilizadores: " + e.getMessage());
        }
    }

    public List<Eliminatoria> getListaEliminatorias() {
        return listaEliminatorias;
    }

    public boolean isFaseGruposConcluida() {
        if (jogos.isEmpty()) return false;

        for (Jogo jogo : jogos) {
            // IDs menores que 1000 são da fase de grupos.
            if (jogo.getId() < 1000) {
                // Em vez de confiar no "isTerminado()", verificamos se existe resultado no ficheiro!
                if (getResultadoJogo(jogo.getId()) == null) {
                    // Isto imprime na consola qual é o jogo que o sistema acha que falta, para te ajudar
                    System.out.println("O sistema bloqueou porque falta o resultado do jogo ID: " + jogo.getId());
                    return false;
                }
            }
        }
        return true;
    }


    public void gerarDezasseisAvosDeFinal() {
        listaEliminatorias.clear();

        // Arrays para guardar as stats temporárias: [0] = Pontos, [1] = Saldo, [2] = Marcados
        java.util.Map<Integer, int[]> stats = new java.util.HashMap<>();
        for (Equipa e : equipas) stats.put(e.getId(), new int[]{0, 0, 0});

        // 1. LER OS RESULTADOS EXATAMENTE COMO NO PAINEL GRUPOS
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("resultados.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                if (p.length < 3) continue;
                int idJogo = Integer.parseInt(p[0]);
                int gc = Integer.parseInt(p[1]);
                int gf = Integer.parseInt(p[2]);

                for (Jogo j : jogos) {
                    if (j.getId() == idJogo) {
                        int idCasa = j.getEquipaCasa().getId();
                        int idFora = j.getEquipaFora().getId();

                        int[] sCasa = stats.get(idCasa);
                        int[] sFora = stats.get(idFora);

                        sCasa[1] += (gc - gf); // Saldo
                        sFora[1] += (gf - gc); // Saldo
                        sCasa[2] += gc;        // Marcados
                        sFora[2] += gf;        // Marcados

                        if (gc > gf) sCasa[0] += 3;
                        else if (gc < gf) sFora[0] += 3;
                        else { sCasa[0] += 1; sFora[0] += 1; }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler resultados: " + e.getMessage());
        }

        // 2. CRUZAMENTOS DO SORTEIO (8 Jogos = 16 Equipas)
        String[][] cruzamentos = {
                {"A", "B"}, {"C", "D"}, {"E", "F"}, {"G", "H"},
                {"I", "J"}, {"K", "L"}, {"B", "A"}, {"D", "C"}
        };

        int idElim = 1000;
        for (String[] par : cruzamentos) {
            java.util.List<Equipa> g1 = new java.util.ArrayList<>();
            java.util.List<Equipa> g2 = new java.util.ArrayList<>();

            for (Equipa e : equipas) {
                if (e.getGrupo().equals(par[0])) g1.add(e);
                if (e.getGrupo().equals(par[1])) g2.add(e);
            }

            // LÓGICA DE DESEMPATE ABSOLUTA
            java.util.Comparator<Equipa> comp = (e1, e2) -> {
                int[] st1 = stats.get(e1.getId());
                int[] st2 = stats.get(e2.getId());
                if (st1[0] != st2[0]) return Integer.compare(st2[0], st1[0]); // Pontos
                if (st1[1] != st2[1]) return Integer.compare(st2[1], st1[1]); // Saldo Golos
                return Integer.compare(st2[2], st1[2]);                       // Golos Marcados
            };

            g1.sort(comp);
            g2.sort(comp);

            if (!g1.isEmpty() && g2.size() >= 2) {
                Equipa primeiro = g1.get(0);
                Equipa segundo = g2.get(1);

                idElim++;
                Jogo j = new Jogo(idElim, primeiro, segundo, estadios.get(0), "28/06/2026", "20:00");
                listaEliminatorias.add(new Eliminatoria(idElim, "Oitavos", j));
            }
        }
    }



    public void setUtilizadorLogado(Utilizador u) {
        this.utilizadorLogado = u;
    }
    public boolean isAdmin() {
        if (utilizadorLogado == null) return false;
        return utilizadorLogado.getTipo().equalsIgnoreCase("ADMIN");
    }





    public List<Utilizador> getUtilizadores() { return utilizadores; }
    // ==========================================
    // 5. GETTERS BÁSICOS
    // ==========================================

    public List<Equipa> getEquipas() { return equipas; }
    public List<Estadio> getEstadios() { return estadios; }
    public List<Jogo> getJogos() { return jogos; }
    public List<Jogador> getJogadores() { return jogadores; }
    public List<Arbitro> getArbitros() { return arbitros; }
    public List<Alojamento> getAlojamentos() { return alojamentos; }


    // ==========================================
    // 6. GERAÇÃO DE DADOS BASE
    // ==========================================
    public void recarregarEstatisticas() {
        estatisticas.clear(); // Limpa a memória antiga
        carregarEstatisticasDoFicheiro(); // Lê o .txt com os novos golos!
    }
    private void gerarEquipas() {
        // Grupo A
        equipas.add(new Equipa(1, "México", "México", "A"));
        equipas.add(new Equipa(2, "Chéquia", "Chéquia", "A"));
        equipas.add(new Equipa(3, "Coreia do Sul", "Coreia do Sul", "A"));
        equipas.add(new Equipa(4, "África do Sul", "África do Sul", "A"));
        // Grupo B
        equipas.add(new Equipa(5, "Canadá", "Canadá", "B"));
        equipas.add(new Equipa(6, "Suíça", "Suíça", "B"));
        equipas.add(new Equipa(7, "Bósnia-Herzegovina", "Bósnia-Herzegovina", "B"));
        equipas.add(new Equipa(8, "Catar", "Catar", "B"));
        // Grupo C
        equipas.add(new Equipa(9, "Brasil", "Brasil", "C"));
        equipas.add(new Equipa(10, "Marrocos", "Marrocos", "C"));
        equipas.add(new Equipa(11, "Escócia", "Escócia", "C"));
        equipas.add(new Equipa(12, "Haiti", "Haiti", "C"));
        // Grupo D
        equipas.add(new Equipa(13, "Estados Unidos", "Estados Unidos", "D"));
        equipas.add(new Equipa(14, "Turquia", "Turquia", "D"));
        equipas.add(new Equipa(15, "Paraguai", "Paraguai", "D"));
        equipas.add(new Equipa(16, "Austrália", "Austrália", "D"));
        // Grupo E
        equipas.add(new Equipa(17, "Alemanha", "Alemanha", "E"));
        equipas.add(new Equipa(18, "Costa do Marfim", "Costa do Marfim", "E"));
        equipas.add(new Equipa(19, "Equador", "Equador", "E"));
        equipas.add(new Equipa(20, "Curaçau", "Curaçau", "E"));
        // Grupo F
        equipas.add(new Equipa(21, "Países Baixos", "Países Baixos", "F"));
        equipas.add(new Equipa(22, "Japão", "Japão", "F"));
        equipas.add(new Equipa(23, "Suécia", "Suécia", "F"));
        equipas.add(new Equipa(24, "Tunísia", "Tunísia", "F"));
        // Grupo G
        equipas.add(new Equipa(25, "Bélgica", "Bélgica", "G"));
        equipas.add(new Equipa(26, "Egito", "Egito", "G"));
        equipas.add(new Equipa(27, "Irão", "Irão", "G"));
        equipas.add(new Equipa(28, "Nova Zelândia", "Nova Zelândia", "G"));
        // Grupo H
        equipas.add(new Equipa(29, "Espanha", "Espanha", "H"));
        equipas.add(new Equipa(30, "Uruguai", "Uruguai", "H"));
        equipas.add(new Equipa(31, "Arábia Saudita", "Arábia Saudita", "H"));
        equipas.add(new Equipa(32, "Cabo Verde", "Cabo Verde", "H"));
        // Grupo I
        equipas.add(new Equipa(33, "França", "França", "I"));
        equipas.add(new Equipa(34, "Noruega", "Noruega", "I"));
        equipas.add(new Equipa(35, "Senegal", "Senegal", "I"));
        equipas.add(new Equipa(36, "Iraque", "Iraque", "I"));
        // Grupo J
        equipas.add(new Equipa(37, "Argentina", "Argentina", "J"));
        equipas.add(new Equipa(38, "Áustria", "Áustria", "J"));
        equipas.add(new Equipa(39, "Argélia", "Argélia", "J"));
        equipas.add(new Equipa(40, "Jordânia", "Jordânia", "J"));
        // Grupo K
        equipas.add(new Equipa(41, "Portugal", "Portugal", "K"));
        equipas.add(new Equipa(42, "Colômbia", "Colômbia", "K"));
        equipas.add(new Equipa(43, "RD Congo", "RD Congo", "K"));
        equipas.add(new Equipa(44, "Uzbequistão", "Uzbequistão", "K"));
        // Grupo L
        equipas.add(new Equipa(45, "Inglaterra", "Inglaterra", "L"));
        equipas.add(new Equipa(46, "Croácia", "Croácia", "L"));
        equipas.add(new Equipa(47, "Gana", "Gana", "L"));
        equipas.add(new Equipa(48, "Panamá", "Panamá", "L"));
    }
}









