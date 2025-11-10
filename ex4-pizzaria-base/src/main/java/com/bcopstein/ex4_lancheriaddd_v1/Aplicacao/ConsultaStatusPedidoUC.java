package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class ConsultaStatusPedidoUC {
    private final PedidoService pedidoService;

    @Autowired
    public ConsultaStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PedidoStatusResponse executar(long idPedido) {
        Pedido pedido = pedidoService.buscaPorId(idPedido);

        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado!");
        }

        return new PedidoStatusResponse(pedido);
    }
}

