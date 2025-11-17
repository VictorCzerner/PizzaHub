/**
 * Classe de teste unitário para AprovaPedidoUC
 *
 * Casos de teste:
 * 1. Cliente existente e pedido aprovado → deve retornar PedidoResponse com status APROVADO.
 * 2. Cliente inexistente → deve lançar RuntimeException.
 * 3. Pedido cancelado → deve retornar lista de itens em falta.
 */

package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AprovaPedidoUCTest {

    private PedidoService pedidoService;
    private ClienteService clienteService;
    private AprovaPedidoUC aprovaPedidoUC;

    @BeforeEach
    public void setUp() {
        pedidoService = mock(PedidoService.class);
        clienteService = mock(ClienteService.class);
        aprovaPedidoUC = new AprovaPedidoUC(pedidoService, clienteService);
    }

    @Test
    public void deveAprovarPedidoQuandoClienteExiste() {
        PedidoRequest request = new PedidoRequest();
        request.setClienteCpf("12345678900");
        request.setItens(List.of());

        Cliente cliente = new Cliente(
            "12345678900",
            "Fulano da Silva",
            "51999999999",
            "Rua das Flores, 123",
            "fulano@email.com",
            "senha",
            Role.CLIENTE
        );

        Pedido pedido = new Pedido(
            1L,
            cliente,
            LocalDateTime.now(),
            List.of(),
            Status.APROVADO,
            100.0,
            10.0,
            5.0,
            105.0
        );

        when(clienteService.buscaPorCPF("12345678900")).thenReturn(cliente);
        when(pedidoService.criaPedido(request)).thenReturn(pedido);
        when(pedidoService.aprovaPedido(pedido)).thenReturn(pedido);

        PedidoResponse response = aprovaPedidoUC.executar(request);

        assertNotNull(response);
        assertEquals("APROVADO", response.getStatus());
        verify(clienteService).buscaPorCPF("12345678900");
        verify(pedidoService).criaPedido(request);
    }

    @Test
    public void deveLancarErroQuandoClienteNaoExiste() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setClienteCpf("99999999999");
        request.setItens(List.of());

        when(clienteService.buscaPorCPF("99999999999")).thenReturn(null);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> aprovaPedidoUC.executar(request));
        assertTrue(ex.getMessage().contains("Cliente não encontrado"));
    }


    @Test
    public void deveRetornarItensEmFaltaQuandoPedidoCancelado() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setClienteCpf("12345678900");
        request.setItens(List.of());

        Cliente cliente = new Cliente(
            "12345678900",          
            "Fulano da Silva",      
            "51999999999",          
            "Rua das Flores, 123",  
            "fulano@email.com",
            "senha",
            Role.CLIENTE      
        );

        Pedido pedido = new Pedido(
            2L,                     
            cliente,                
            LocalDateTime.now(),    
            List.of(),             
            Status.CANCELADO,       
            100.0,                  
            10.0,                   
            5.0,                    
            105.0                  
        );

        ItemPedido itemFaltando = mock(ItemPedido.class);
        Produto produto = mock(Produto.class);

        when(produto.getDescricao()).thenReturn("Coca-Cola");
        when(itemFaltando.getItem()).thenReturn(produto);
        when(itemFaltando.getQuantidade()).thenReturn(2);


        when(clienteService.buscaPorCPF("12345678900")).thenReturn(cliente);
        when(pedidoService.criaPedido(request)).thenReturn(pedido);
        when(pedidoService.aprovaPedido(pedido)).thenReturn(pedido);
        when(pedidoService.verificaItens(pedido)).thenReturn(List.of(itemFaltando));

        // Act
        PedidoResponse response = aprovaPedidoUC.executar(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getItensEmFalta());
        assertEquals("Coca-Cola", response.getItensEmFalta().get(0).getDescricao());
    }
}
