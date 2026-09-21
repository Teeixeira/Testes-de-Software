package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void deveCriarClienteValido() {
        Cliente c = new Cliente(true, false, 5);
        assertTrue(c.vip());
        assertFalse(c.bloqueado());
        assertEquals(5, c.comprasAnteriores());
    }

    @Test
    void naoDeveCriarClienteComHistoricoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(false, false, -1));
    }
}