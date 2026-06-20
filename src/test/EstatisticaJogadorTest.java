package test;

import model.EstatisticaJogador;
import model.Jogador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstatisticaJogadorTest {

    private Jogador jogadorMock;

    @BeforeEach
    void setUp() {
        // Criamos um jogador "falso" apenas para podermos testar as estatísticas
        jogadorMock = new Jogador(1, "Cristiano Ronaldo", "Portugal", "Al Nassr", "Avançado", 1.87, 83.0, 90, 7);
    }

    @Test
    void testCriarEstatisticaComSucesso() {
        EstatisticaJogador stats = new EstatisticaJogador(jogadorMock, "Espanha", 2, 1, 0, 9.5, "15/06/2026");

        // Verifica se guardou tudo bem
        assertEquals(2, stats.getGolos());
        assertEquals(9.5, stats.getRating());
        assertEquals("Espanha", stats.getAdversario());
    }

    @Test
    void testNaoPermitirGolosOuAssistenciasNegativas() {
        // Tentar criar estatística com -1 golo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EstatisticaJogador(jogadorMock, "Espanha", -1, 0, 0, 8.0, "15/06/2026");
        });

        assertEquals("As estatísticas não podem ser negativas.", exception.getMessage());
    }

    @Test
    void testNaoPermitirRatingAcimaDeDez() {
        // Tentar dar um rating de 11.5 (o limite é 10)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EstatisticaJogador(jogadorMock, "Espanha", 2, 0, 0, 11.5, "15/06/2026");
        });

        assertEquals("O rating deve estar entre 0 e 10.", exception.getMessage());
    }

    @Test
    void testNaoPermitirRatingAbaixoDeZero() {
        // Tentar dar um rating negativo
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EstatisticaJogador(jogadorMock, "Espanha", 0, 0, 0, -2.0, "15/06/2026");
        });

        assertEquals("O rating deve estar entre 0 e 10.", exception.getMessage());
    }

    @Test
    void testNaoPermitirEstatisticaSemJogador() {
        // Tentar passar "null" no lugar do jogador
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EstatisticaJogador(null, "Espanha", 1, 0, 0, 7.5, "15/06/2026");
        });

        assertEquals("O jogador é obrigatório.", exception.getMessage());
    }
}