package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {
    @Test
    void deveCriarItemValidoECalcularTotalCentavos() {
        ItemPedido item = new ItemPedido("SKU1", 1000, 2, 5, 500, true);
        assertEquals(2000, item.totalCentavos());
        assertTrue(item.disponivel());
    }

    @Test
    void deveRetornarIndisponivelSeQuantidadeForMaiorQueOEstoque() {
        ItemPedido item = new ItemPedido("SKU1", 1000, 5, 2, 500, false);
        assertFalse(item.disponivel());
    }

    @Test
    void deveLancarExcecaoParaEntradasDeConstrucaoInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido(null, 1000, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido(" ", 1000, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 0, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000001, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000, -1, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000, 101, 1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000, 1, -1, 100, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000, 1, 1, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000, 1, 1, 100001, false));
    }
}