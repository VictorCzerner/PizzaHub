package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
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

    public PedidoStatusResponse executar(Long id) {
        Pedido pedido = pedidoService.buscaPorId(id);
        pedido.setStatus(Pedido.Status.PAGO);
        pedidoService.atualizaPedido(pedido);

        // Envia para a cozinha e deixa ela cuidar do fluxo (PREPARACAO → PRONTO → SAÍDA)
        cozinhaService.chegadaDePedido(pedido);

        System.out.println("Pedido enviado para a cozinha");

        // Aqui o status final ainda é PAGO, a cozinha vai mudar depois
        return new PedidoStatusResponse(pedido);
    }
}
