package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagarPedidoUC
{
    private PedidoService pedidoService;
    private CozinhaService cozinhaService;

    @Autowired
    public PagarPedidoUC(PedidoService pedidoService, CozinhaService cozinhaService)
    {
        this.pedidoService = pedidoService;
        this.cozinhaService = cozinhaService;
    }

    public PedidoStatusResponse executar(PedidoRequest pedidoRequest)
    {
        Pedido pedido = pedidoService.criaPedido(pedidoRequest);

        cozinhaService.chegadaDePedido(pedido);

        pedido.setStatus(Pedido.Status.TRANSPORTE);
        System.out.println("Pedido em rota de entrega");
        
        pedido.setStatus(Pedido.Status.ENTREGUE);
        System.out.println("Pedido entregue");

        return new PedidoStatusResponse(pedido);
    }
}
