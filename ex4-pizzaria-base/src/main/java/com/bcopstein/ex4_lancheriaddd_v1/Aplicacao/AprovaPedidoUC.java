package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

            pedido = pedidoService.aprovaPedido(pedido);
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
