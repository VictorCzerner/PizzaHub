package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

// ===========================================================
// 1️ Pedido com um item → sem desconto → imposto 10% → total 110
// 2️ Pedido sem itens → total esperado 0
// 3️ Pedido com desconto aplicado → valor base 100 → desconto 7% → total 93
// 4️ Itens em falta no estoque
// 5️ Estoque suficiente → nenhum item em falta
// ===========================================================

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    private PedidoService pedidoService;
    private PedidoRepository pedidoRepository;
    private ItensEstoqueService itensEstoqueService;
    private ProdutosRepository produtosRepository;
    private ImpostoService impostoService;
    private DescontoService descontoService;

    @BeforeEach
    public void setup() {
        pedidoRepository = mock(PedidoRepository.class);
        itensEstoqueService = mock(ItensEstoqueService.class);
        produtosRepository = mock(ProdutosRepository.class);
        impostoService = mock(ImpostoService.class);
        descontoService = mock(DescontoService.class);

        pedidoService = new PedidoService(
            pedidoRepository,
            itensEstoqueService,
            produtosRepository,
            impostoService,
            descontoService
        );
    }


    @Test
    public void deveCalcularCustoCorretamenteSemDesconto() {
        Cliente cliente = new Cliente(
            "12345678900", "Fulano", "51999999999", "Rua das Flores, 123", "fulano@email.com", "senha", Role.CLIENTE
        );

        Produto produto = mock(Produto.class);
        when(produto.getPreco()).thenReturn(10000); // 100.00 reais

        ItemPedido item = new ItemPedido(produto, 1);
        Pedido pedido = new Pedido(
            1L, cliente, LocalDateTime.now(),
            List.of(item), Pedido.Status.APROVADO,
            0.0, 0.0, 0.0, 0.0
        );

        when(descontoService.aplicarDescontoAtivo(cliente, pedido)).thenReturn(100.0);
        when(impostoService.calcularImpostos(100.0)).thenReturn(10.0);

        double total = pedidoService.calculaCusto(cliente, pedido);

        assertEquals(110.0, total, 0.001);
        verify(descontoService, times(1)).aplicarDescontoAtivo(cliente, pedido);
        verify(impostoService, times(1)).calcularImpostos(100.0);
    }

    
    @Test
    public void deveRetornarZeroQuandoNaoHaItens() {
        Cliente cliente = new Cliente("12345678900", "Fulano", "51999999999", "Rua X", "email@email.com", "senha", Role.CLIENTE);
        Pedido pedido = new Pedido(
            2L, cliente, LocalDateTime.now(),
            List.of(), Pedido.Status.NOVO,
            0.0, 0.0, 0.0, 0.0
        );

        double total = pedidoService.calculaCusto(cliente, pedido);

        assertEquals(0.0, total, 0.001);
        assertEquals(0.0, pedido.getValor(), 0.001);
    }


    @Test
    public void deveCalcularCustoComDesconto() {
        Cliente cliente = new Cliente("98765432100", "Maria", "51988888888", "Rua Azul, 99", "maria@email.com", "senha", Role.CLIENTE);

        Produto produto = mock(Produto.class);
        when(produto.getPreco()).thenReturn(10000); // 100 reais

        ItemPedido item = new ItemPedido(produto, 1);
        Pedido pedido = new Pedido(
            3L, cliente, LocalDateTime.now(),
            List.of(item), Pedido.Status.APROVADO,
            0.0, 0.0, 0.0, 0.0
        );

        // Aplica desconto de 7%
        when(descontoService.aplicarDescontoAtivo(cliente, pedido)).thenReturn(93.0);
        when(impostoService.calcularImpostos(93.0)).thenReturn(0.0);

        double total = pedidoService.calculaCusto(cliente, pedido);

        assertEquals(93.0, total, 0.001);
        verify(descontoService, times(1)).aplicarDescontoAtivo(cliente, pedido);
    }


    @Test
    public void deveRetornarItensEmFaltaQuandoEstoqueInsuficiente() {
        Ingrediente queijo = new Ingrediente(1, "Queijo");
        Receita receita = mock(Receita.class);
        when(receita.getIngredientes()).thenReturn(List.of(queijo));

        Produto produto = mock(Produto.class);
        when(produto.getDescricao()).thenReturn("Pizza Mussarela");
        when(produto.getReceita()).thenReturn(receita);

        ItemPedido itemPedido = new ItemPedido(produto, 5);
        Pedido pedido = new Pedido(10, new Cliente("12345678900"), LocalDateTime.now(), List.of(itemPedido), Pedido.Status.NOVO, 0, 0, 0, 0);

        when(itensEstoqueService.consultarQuantidade(1)).thenReturn(2);

        List<ItemPedido> faltantes = pedidoService.verificaItens(pedido);

        assertEquals(1, faltantes.size());
        assertEquals("Pizza Mussarela", faltantes.get(0).getItem().getDescricao());
    }


    @Test
    public void naoDeveRetornarItensEmFaltaQuandoEstoqueSuficiente() {
        Ingrediente molho = new Ingrediente(2, "Molho de Tomate");
        Receita receita = mock(Receita.class);
        when(receita.getIngredientes()).thenReturn(List.of(molho));

        Produto produto = mock(Produto.class);
        when(produto.getDescricao()).thenReturn("Pizza Calabresa");
        when(produto.getReceita()).thenReturn(receita);

        ItemPedido itemPedido = new ItemPedido(produto, 3);
        Pedido pedido = new Pedido(11, new Cliente("98765432100"), LocalDateTime.now(), List.of(itemPedido), Pedido.Status.NOVO, 0, 0, 0, 0);

        when(itensEstoqueService.consultarQuantidade(2)).thenReturn(10);

        List<ItemPedido> faltantes = pedidoService.verificaItens(pedido);

        assertTrue(faltantes.isEmpty());
    }
}
