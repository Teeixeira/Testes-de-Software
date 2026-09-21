package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    @Test
    void deveCalcularSubtotalPesoEVerificarDisponibilidadeDeEstoque() {
        ItemPedido i1 = new ItemPedido("SKU1", 1000, 2, 5, 500, false);
        ItemPedido inativo = new ItemPedido("SKU2", 2000, 0, 0, 1000, true);
        Pedido p = new Pedido(List.of(i1, inativo), "PR", false, null);

        assertEquals(2000, p.subtotalCentavos());
        assertEquals(1000, p.pesoGramas());
        assertFalse(p.temFragil());
        assertTrue(p.estoqueSuficiente());
    }

    @Test
    void deveIdentificarItemFragilEEstoqueInsuficiente() {
        ItemPedido indisponivel = new ItemPedido("SKU1", 1000, 2, 1, 500, true);
        Pedido p = new Pedido(List.of(indisponivel), "SP", true, "CUPOM");

        assertTrue(p.temFragil());
        assertFalse(p.estoqueSuficiente());
    }

    @Test
    void deveLancarExcecaoParaListaItensOuUfInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Pedido(null, "PR", false, null));
        List<ItemPedido> itens = new ArrayList<>();
        for(int i=0; i<101; i++) itens.add(new ItemPedido("S", 10, 1, 1, 10, false));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(itens, "PR", false, null));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(), null, false, null));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(), "pr", false, null));
        assertThrows(IllegalArgumentException.class, () -> new Pedido(List.of(), "PAR", false, null));
    }
}