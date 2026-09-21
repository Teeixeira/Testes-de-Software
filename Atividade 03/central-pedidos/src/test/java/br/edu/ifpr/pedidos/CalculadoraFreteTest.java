package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {
    private final CalculadoraFrete calc = new CalculadoraFrete();
    private final Cliente comum = new Cliente(false, false, 1);
    private final Cliente vip = new Cliente(true, false, 1);
    private final ItemPedido item2kg = new ItemPedido("A", 100, 1, 1, 2000, false);
    private final ItemPedido item3kgFragil = new ItemPedido("B", 100, 1, 1, 3000, true);
    private final ItemPedido item3001g = new ItemPedido("C", 100, 1, 1, 3001, false);

    @Test
    void deveLancarExcecaoParaValorLiquidoNegativo() {
        Pedido p = new Pedido(List.of(item2kg), "PR", false, null);
        assertThrows(IllegalArgumentException.class, () -> calc.calcular(p, comum, -1));
    }

    @Test
    void deveCalcularFreteBaseCorretamentePorRegiao() {
        Pedido pPR = new Pedido(List.of(item2kg), "PR", false, null);
        Pedido pSP = new Pedido(List.of(item2kg), "SP", false, null);
        Pedido pMG = new Pedido(List.of(item2kg), "MG", false, null);

        assertEquals(1_200, calc.calcular(pPR, comum, 10_000));
        assertEquals(2_000, calc.calcular(pSP, comum, 10_000));
        assertEquals(3_000, calc.calcular(pMG, comum, 10_000));
    }

    @Test
    void deveAplicarLaçoDeExcessoDePesoCorretamente() {
        Pedido p3kg = new Pedido(List.of(item3kgFragil), "PR", false, null);
        assertEquals(2_000, calc.calcular(p3kg, comum, 10_000)); // PR(1200) + Peso(300) + Fragil(500)

        Pedido p3001g = new Pedido(List.of(item3001g), "PR", false, null);
        assertEquals(1_800, calc.calcular(p3001g, comum, 10_000)); // PR(1200) + 2x Peso(600)
    }

    @Test
    void deveZerarasBasesEmFreteGratisMantendoAdicionais() {
        Pedido pGratis = new Pedido(List.of(item3kgFragil), "PR", false, null);
        assertEquals(500, calc.calcular(pGratis, comum, 30_000)); // Zera PR e Peso. Aplica Fragil(500)

        Pedido pExpresso = new Pedido(List.of(item3kgFragil), "PR", true, null);
        assertEquals(3_500, calc.calcular(pExpresso, comum, 30_000)); // Não zera por ser Expresso
    }

    @Test
    void deveCortarOValorPelaMetadeParaClientesVip() {
        Pedido p = new Pedido(List.of(item2kg), "PR", true, null);
        assertEquals(2_100, calc.calcular(p, vip, 10_000)); // (PR 1200 / 2) + Expresso(1500)
    }
}