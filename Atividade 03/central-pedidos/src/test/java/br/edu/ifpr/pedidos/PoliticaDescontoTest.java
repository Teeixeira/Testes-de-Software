package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {
    private final PoliticaDesconto desc = new PoliticaDesconto();

    @Test
    void deveLancarExcecaoParaSubtotalNegativo() {
        Cliente c = new Cliente(false, false, 0);
        assertThrows(IllegalArgumentException.class, () -> desc.calcular(c, -1, null));
    }

    @Test
    void deveAplicarDezPorcentoParaVip() {
        Cliente vip = new Cliente(true, false, 5);
        assertEquals(4_000, desc.calcular(vip, 40_000, null));
    }

    @Test
    void deveAplicarCincoPorcentoParaComumAcimaDoLimite() {
        Cliente comum = new Cliente(false, false, 5);
        assertEquals(2_500, desc.calcular(comum, 50_000, null));
        assertEquals(0, desc.calcular(comum, 49_999, null));
    }

    @Test
    void deveAplicarCupomBemVindoSobRegras() {
        Cliente novo = new Cliente(false, false, 0);
        Cliente antigo = new Cliente(false, false, 1);
        assertEquals(2_000, desc.calcular(novo, 10_000, "BEMVINDO"));
        assertEquals(0, desc.calcular(novo, 9_999, "BEMVINDO"));
        assertEquals(0, desc.calcular(antigo, 10_000, "BEMVINDO"));
    }

    @Test
    void deveAplicarCupomExtraDezSobRegras() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(2_000, desc.calcular(comum, 20_000, "EXTRA10"));
        assertEquals(0, desc.calcular(comum, 19_999, "EXTRA10"));
    }

    @Test
    void deveRespeitarOTetoGlobalDeVintePorcento() {
        Cliente vip = new Cliente(true, false, 0);
        assertEquals(5_000, desc.calcular(vip, 30_000, "BEMVINDO")); // 10% (3000) + 2000 = 5000 (Teto 6000)
        assertEquals(3_000, desc.calcular(vip, 15_000, "BEMVINDO")); // 10% (1500) + 2000 = 3500 (Teto limitou para 3000)
    }

    @Test
    void deveLancarExcecaoParaCupomDesconhecidoEValidarNormalizacao() {
        Cliente comum = new Cliente(false, false, 1);
        assertEquals(2_000, desc.calcular(comum, 20_000, "  extra10 "));
        assertEquals(0, desc.calcular(comum, 10_000, ""));
        assertThrows(IllegalArgumentException.class, () -> desc.calcular(comum, 10_000, "INVALIDO"));
    }
}