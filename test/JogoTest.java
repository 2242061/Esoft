import model.Arbitro;
import model.Equipa;
import model.Estadio;
import model.Jogo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JogoTest {

    @Test
    public void deveCriarJogoComSucesso() {
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Equipa espanha = new Equipa(2, "Espanha", "Espanha", "A");
        Estadio estadio = new Estadio(1, "Estádio de Alvalade", "Lisboa", 50000);

        Jogo jogo = new Jogo(1, portugal, espanha, estadio, "20/05/2026", "20:30");

        assertEquals("Portugal", jogo.getEquipaCasa().getNome());
        assertEquals("Espanha", jogo.getEquipaFora().getNome());
        assertEquals("Estádio de Alvalade", jogo.getEstadio().getNome());
        assertFalse(jogo.isTerminado());
    }

    @Test
    public void naoDeveCriarJogoComMesmaEquipa() {
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Estadio estadio = new Estadio(1, "Estádio de Alvalade", "Lisboa", 50000);

        assertThrows(IllegalArgumentException.class, () -> {
            new Jogo(1, portugal, portugal, estadio, "20/05/2026", "20:30");
        });
    }

    @Test
    public void deveAtribuirArbitroAoJogo() {
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Equipa espanha = new Equipa(2, "Espanha", "Espanha", "A");
        Estadio estadio = new Estadio(1, "Estádio de Alvalade", "Lisboa", 50000);
        Arbitro arbitro = new Arbitro(1, "Artur Silva", "Portugal", 10);

        Jogo jogo = new Jogo(1, portugal, espanha, estadio, "20/05/2026", "20:30");
        jogo.atribuirArbitro(arbitro);

        assertNotNull(jogo.getArbitro());
        assertEquals("Artur Silva", jogo.getArbitro().getNome());
    }

    @Test
    public void deveRegistarResultadoEVencedor() {
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Equipa espanha = new Equipa(2, "Espanha", "Espanha", "A");
        Estadio estadio = new Estadio(1, "Estádio de Alvalade", "Lisboa", 50000);

        Jogo jogo = new Jogo(1, portugal, espanha, estadio, "20/05/2026", "20:30");
        jogo.registarResultado(2, 1);

        assertTrue(jogo.isTerminado());
        assertEquals(2, jogo.getGolosCasa());
        assertEquals(1, jogo.getGolosFora());
        assertEquals(portugal, jogo.obterVencedor());
    }

    @Test
    public void naoDeveRegistarResultadoComGolosNegativos() {
        Equipa portugal = new Equipa(1, "Portugal", "Portugal", "A");
        Equipa espanha = new Equipa(2, "Espanha", "Espanha", "A");
        Estadio estadio = new Estadio(1, "Estádio de Alvalade", "Lisboa", 50000);

        Jogo jogo = new Jogo(1, portugal, espanha, estadio, "20/05/2026", "20:30");

        assertThrows(IllegalArgumentException.class, () -> {
            jogo.registarResultado(-1, 2);
        });
    }
}