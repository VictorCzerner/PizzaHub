/**
 * Testes unitários da função calculaCusto() da classe PedidoService
 *
 * Casos de teste:
 * 1. Pedido com um item de preço 100 → aplica desconto 10%, imposto 5% → total esperado 94.5.
 * 2. Pedido sem itens → total esperado 0.
 * 3. Pedido com 2 itens → soma correta dos preços e aplicação dos serviços de desconto e imposto.
 */

package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoServiceTest {

    private PedidoService pedidoService;
    private PedidoRepository pedidoRepository;
    private ItensEstoqueRepository itensEstoqueRepository;
    private ProdutosRepository produtosRepository;
    private ImpostoService impostoService;
    private DescontoService descontoService;

    @BeforeEach
    public void setup() {
        // cria mocks para todas as dependências exigidas no construtor
        pedidoRepository = mock(PedidoRepository.class);
        itensEstoqueRepository = mock(ItensEstoqueRepository.class);
        produtosRepository = mock(ProdutosRepository.class);
        impostoService = mock(ImpostoService.class);
        descontoService = mock(DescontoService.class);

        // instancia o serviço com todos os mocks
        pedidoService = new PedidoService(
            pedidoRepository,
            itensEstoqueRepository,
            produtosRepository,
            impostoService,
            descontoService
        );
    }

    @Test
    public void deveCalcularCustoCorretamenteComDescontoEImposto() {
        // Arrange
        Cliente cliente = new Cliente(
            "12345678900",
            "Fulano da Silva",
            "51999999999",
            "Rua das Flores, 123",
            "fulano@email.com"
        );

        Produto produto = mock(Produto.class);
        when(produto.getPreco()).thenReturn(10000);

        ItemPedido item = new ItemPedido(produto, 1);
        Pedido pedido = new Pedido(
            1L, cliente, LocalDateTime.now(),
            List.of(item), Pedido.Status.APROVADO,
            0.0, 0.0, 0.0, 0.0
        );

        // Simula comportamento dos mocks
        when(pedidoRepository.quantidadePedidosUltimos20Dias("12345678900")).thenReturn(2);
        when(descontoService.aplicarDesconto(anyDouble(), eq(2)))
            .thenAnswer(i -> i.getArgument(0, Double.class) * 0.9); // 10% desconto
        when(descontoService.getPercentualDesconto(2)).thenReturn(10.0);
        when(impostoService.calcularImpostos(anyDouble())).thenReturn(4.5); // imposto fixo 4.5

        // Act
        double total = pedidoService.calculaCusto(pedido);

        // Assert
        assertEquals(94.5, total, 0.001);
        assertEquals(10.0, pedido.getDesconto(), 0.001);
        assertEquals(4.5, pedido.getImpostos(), 0.001);

        // verifica se métodos foram chamados
        verify(pedidoRepository).quantidadePedidosUltimos20Dias("12345678900");
        verify(descontoService).aplicarDesconto(anyDouble(), eq(2));
        verify(impostoService).calcularImpostos(anyDouble());
    }
}