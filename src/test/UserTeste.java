package test;

import model.Utilizador;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTeste {

    @Test
    void testAutenticacaoComSucesso() {
        Utilizador user = new Utilizador(1, "Miguel", "miguel@user.pt", "1234", "USER");

        // Verifica se a password correta dá true
        assertTrue(user.autenticar("miguel@user.pt", "1234"));
    }

    @Test
    void testAutenticacaoFalhada() {
        Utilizador user = new Utilizador(1, "Miguel", "miguel@user.pt", "1234", "USER");

        // Verifica se uma password errada dá false
        assertFalse(user.autenticar("miguel@user.pt", "passwordErrada"));
    }

    @Test
    void testValidacaoIsAdmin() {
        Utilizador admin = new Utilizador(1, "Admin", "admin@mundial.pt", "admin123", "ADMIN");
        Utilizador user = new Utilizador(2, "Adepto", "adepto@mundial.pt", "adepto123", "USER");

        assertTrue(admin.isAdmin(), "O administrador deveria retornar true no isAdmin()");
        assertFalse(user.isAdmin(), "O utilizador comum deveria retornar false no isAdmin()");
    }

    @Test
    void testNaoPermitirPasswordCurta() {
        // Tentar criar um utilizador com password de 3 caracteres ("123")
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Utilizador(3, "Teste", "teste@teste.pt", "123", "USER");
        });

        assertEquals("A password deve ter pelo menos 4 caracteres.", exception.getMessage());
    }
}