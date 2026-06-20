package test;

import model.Equipa;
import model.Estadio;
import model.Jogo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JogoTeste {

    private Equipa portugal;
    private Equipa espanha;
    private Estadio estadio;

    @BeforeEach
    void setUp() {
        portugal = new Equipa(1, "Portugal", "Portugal", "K");
        espanha = new Equipa(2, "Espanha", "Espanha", "H");
        estadio = new Estadio(1, "Estádio da Luz", "Lisboa", 65000);
    }

    @Test
    void testNaoPermitirEquipaJogarContraSiPropria() {
        // Tentar criar um jogo onde Portugal joga contra Portugal
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Jogo(100, portugal, portugal, estadio, "20/06/2026", "20:00");
        });

        assertEquals("Uma equipa não pode jogar contra si própria.", exception.getMessage());
    }

    @Test
    void testObterVencedorJogoNaoTerminado() {
        Jogo jogo = new Jogo(100, portugal, espanha, estadio, "20/06/2026", "20:00");

        // Como o jogo acabou de ser criado e não está terminado, o vencedor tem de ser nulo
        assertNull(jogo.obterVencedor(), "Um jogo não terminado não deve ter vencedor.");
    }
}