package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {
    @Test
    void deveRejeitarParametrosInvalidosOuDependentesNulos() {
        PagamentoService serv = new PagamentoService(t -> true);
        assertThrows(IllegalArgumentException.class, () -> serv.pagar(0, 1));
        assertThrows(IllegalArgumentException.class, () -> serv.pagar(100, 0));
        assertThrows(IllegalArgumentException.class, () -> serv.pagar(100, 4));
        assertThrows(NullPointerException.class, () -> new PagamentoService(null));
    }

    @Test
    void deveAprovarCorretamenteNaPrimeiraTentativa() {
        PagamentoService serv = new PagamentoService(t -> true);
        assertTrue(serv.pagar(100, 3));
    }

    @Test
    void deveRecusarSemRepeticaoEmCasoDeFalhaDefinitiva() {
        int[] cont = {0};
        PagamentoService serv = new PagamentoService(t -> {
            cont[0]++;
            return false;
        });
        assertFalse(serv.pagar(100, 3));
        assertEquals(1, cont[0]);
    }

    @Test
    void deveEsgotarAsTentativasEmIndisponibilidadeContinua() {
        int[] cont = {0};
        PagamentoService serv = new PagamentoService(t -> {
            cont[0]++;
            throw new IllegalStateException("Offline");
        });
        assertFalse(serv.pagar(100, 3));
        assertEquals(3, cont[0]);
    }

    @Test
    void deveAprovarAposFalhaTemporaria() {
        int[] cont = {0};
        PagamentoService serv = new PagamentoService(t -> {
            cont[0]++;
            if(cont[0] == 1) throw new IllegalStateException("Falha temporária");
            return true;
        });
        assertTrue(serv.pagar(100, 3));
        assertEquals(2, cont[0]);
    }

    @Test
    void devePropagarOutrasExcecoesSemTratar() {
        PagamentoService serv = new PagamentoService(t -> {
            throw new RuntimeException("Erro não catalogado");
        });
        assertThrows(RuntimeException.class, () -> serv.pagar(100, 3));
    }
}