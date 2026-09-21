package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {
    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void deveLancarExcecaoParaTotalNegativo() {
        Cliente c = new Cliente(false, false, 0);
        assertThrows(IllegalArgumentException.class, () -> risco.avaliar(c, -1, false));
    }

    @Test
    void deveRecusarSempreClienteBloqueado() {
        Cliente c = new Cliente(false, true, 0);
        assertEquals("RECUSADO", risco.avaliar(c, 500, false));
    }

    @Test
    void deveAvaliarClientesSemComprasAnteriores() {
        Cliente c = new Cliente(false, false, 0);
        assertEquals("REVISAO", risco.avaliar(c, 100_001, false));
        assertEquals("REVISAO", risco.avaliar(c, 50_000, true));
        assertEquals("APROVADO", risco.avaliar(c, 100_000, false));
    }

    @Test
    void deveAvaliarClientesComComprasAnteriores() {
        Cliente comum = new Cliente(false, false, 1);
        Cliente vip = new Cliente(true, false, 1);
        assertEquals("REVISAO", risco.avaliar(comum, 500_001, false));
        assertEquals("APROVADO", risco.avaliar(vip, 500_001, false));
        assertEquals("APROVADO", risco.avaliar(comum, 500_000, false));
    }
}