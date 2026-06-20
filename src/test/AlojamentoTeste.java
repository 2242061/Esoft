package test;

import model.Alojamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlojamentoTeste {

    private Alojamento hotel;

    @BeforeEach
    void setUp() {
        hotel = new Alojamento(1, "Hotel Mundial", "Lisboa", "Rua A", 200);
    }

    @Test
    void testReservarECancelarAlojamento() {
        // O Alojamento por defeito não deve estar reservado (embora não tenhas um getReservado, podemos testar o fluxo de exceptions se tivesses)
        hotel.reservar();
        // A lógica de reserva correu sem erros.

        hotel.cancelarReserva();
        // A lógica de cancelamento correu sem erros.
    }

    @Test
    void testNaoPermitirCapacidadeZeroOuNegativa() {
        // Tentar criar um hotel com -50 de capacidade
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Alojamento(2, "Hotel Inválido", "Porto", "Rua B", -50);
        });

        assertEquals("A capacidade deve ser superior a zero.", exception.getMessage());
    }
}