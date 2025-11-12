package com.Grupo01.PizzaHUB.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Grupo01.PizzaHUB.Aplicacao.Request.PedidoRequest;
import com.Grupo01.PizzaHUB.Aplicacao.Responses.PedidoResponse;
import com.Grupo01.PizzaHUB.Dominio.Entidades.Cliente;
import com.Grupo01.PizzaHUB.Dominio.Entidades.ItemPedido;
import com.Grupo01.PizzaHUB.Dominio.Entidades.Pedido;
import com.Grupo01.PizzaHUB.Dominio.Entidades.Pedido.Status;
import com.Grupo01.PizzaHUB.Dominio.Servicos.ClienteService;
import com.Grupo01.PizzaHUB.Dominio.Servicos.PedidoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AprovaPedidoUC {
    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    @Autowired
    public AprovaPedidoUC(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    public PedidoResponse executar(PedidoRequest pedidoRequest) {
        try {
            Cliente cliente = clienteService.buscaPorCPF(pedidoRequest.getClienteCpf());
            if (cliente == null) {
                throw new RuntimeException();
            }

            // Cria o pedido apenas se o cliente existir
            Pedido pedido = pedidoService.criaPedido(pedidoRequest);
            List<ItemPedido> itensEmFalta = new ArrayList<>();

            pedido = pedidoService.aprovaPedido(cliente, pedido);
            if (pedido.getStatus() == Status.CANCELADO) {
                itensEmFalta = pedidoService.verificaItens(pedido);
            }

            List<PedidoResponse.ItemResponse> faltantes = itensEmFalta.stream()
                    .map(i -> new PedidoResponse.ItemResponse(i.getItem().getDescricao(), i.getQuantidade()))
                    .collect(Collectors.toList());

            return new PedidoResponse(pedido, faltantes.isEmpty() ? null : faltantes);
        } catch (RuntimeException e) {
            // Pode lançar uma exceção customizada, retornar erro, ou outro comportamento
            throw new RuntimeException("Erro: Cliente não encontrado para o CPF informado.");
        }
    }

}
