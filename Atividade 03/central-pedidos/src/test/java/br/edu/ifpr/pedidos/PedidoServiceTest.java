package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
            () -> assertEquals("PAGO", resultado.status()),
            () -> assertEquals(10_000L, resultado.subtotalCentavos()),
            () -> assertEquals(0L, resultado.descontoCentavos()),
            () -> assertEquals(1_200L, resultado.freteCentavos()),
            () -> assertEquals(11_200L, resultado.totalCentavos()),
            () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    @Test
    void deveRetornarBloqueadoSemProcessarItens() {
        Cliente c = new Cliente(false, true, 1);
        Pedido p = new Pedido(List.of(new ItemPedido("A", 100, 1, 1, 100, false)), "PR", false, null);
        PedidoService serv = new PedidoService(t -> true);

        ResultadoPedido r = serv.fechar(p, c);
        assertEquals("BLOQUEADO", r.status());
        assertEquals(0, r.totalCentavos());
    }

    @Test
    void deveLancarExcecaoParaEntradasNulasOuSubtotalZero() {
        Cliente c = new Cliente(false, false, 1);
        Pedido p = new Pedido(List.of(new ItemPedido("A", 100, 0, 1, 100, false)), "PR", false, null);
        PedidoService serv = new PedidoService(t -> true);

        assertThrows(IllegalArgumentException.class, () -> serv.fechar(p, c));
        assertThrows(NullPointerException.class, () -> serv.fechar(null, c));
        assertThrows(NullPointerException.class, () -> serv.fechar(p, null));
    }

    @Test
    void deveRetornarSemEstoqueSeHouverInsuficiencia() {
        Cliente c = new Cliente(false, false, 1);
        Pedido p = new Pedido(List.of(new ItemPedido("A", 100, 2, 1, 100, false)), "PR", false, null);
        PedidoService serv = new PedidoService(t -> true);

        ResultadoPedido r = serv.fechar(p, c);
        assertEquals("SEM_ESTOQUE", r.status());
        assertEquals(0, r.totalCentavos());
    }

    @Test
    void deveReterProcessamentoParaRevisaoSemTentarCobrar() {
        Cliente c = new Cliente(false, false, 0);
        Pedido p = new Pedido(List.of(new ItemPedido("A", 150_000, 1, 1, 100, false)), "PR", false, null);

        boolean[] cobrado = {false};
        PedidoService serv = new PedidoService(t -> {
            cobrado[0] = true;
            return true;
        });

        ResultadoPedido r = serv.fechar(p, c);
        assertEquals("REVISAO", r.status());
        assertFalse(cobrado[0]);
        assertTrue(r.totalCentavos() > 0);
    }

    @Test
    void deveNotificarPagamentoRecusadoQuandoAInterfaceRetornarFalse() {
        Cliente c = new Cliente(false, false, 1);
        Pedido p = new Pedido(List.of(new ItemPedido("A", 10_000, 1, 1, 100, false)), "PR", false, null);
        PedidoService serv = new PedidoService(t -> false);

        ResultadoPedido r = serv.fechar(p, c);
        assertEquals("PAGAMENTO_RECUSADO", r.status());
        assertEquals(11_200, r.totalCentavos());
    }
}