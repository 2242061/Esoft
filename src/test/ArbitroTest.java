package test;

import model.Arbitro;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArbitroTest {

    @Test
    void testCriacaoArbitroComSucesso() {
        Arbitro arbitro = new Arbitro(1, "Artur Soares Dias", "Portugal", 15);

        assertEquals("Artur Soares Dias", arbitro.getNome());
        assertEquals(15, arbitro.getAnosExperiencia());
    }

    @Test
    void testNaoPermitirNomeVazio() {
        // Tentar criar um árbitro com um nome em branco ""
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Arbitro(2, "", "Espanha", 10);
        });

        assertEquals("O nome do árbitro é obrigatório.", exception.getMessage());
    }

    @Test
    void testNaoPermitirExperienciaNegativa() {
        // Tentar criar um árbitro com -5 anos de experiência
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Arbitro(3, "Pierluigi Collina", "Itália", -5);
        });

        assertEquals("Os anos de experiência não podem ser negativos.", exception.getMessage());
    }
}
