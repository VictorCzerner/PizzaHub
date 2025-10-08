package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponseUC6;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

public class ListaPedidosPorIntervaloUC{
    private PedidoService pedidoService;

    @Autowired
    public ListaPedidosPorIntervaloUC (PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    public List<PedidoResponseUC6> listaPedidosEntregueDatas(LocalDateTime dataInicio, LocalDateTime dataFinal){
        List<Pedido> pedidos = pedidoService.listaPedidosEntreDatas(dataInicio, dataFinal);

        List<PedidoResponseUC6> pedidosResponse = pedidos.stream()
                .filter(p -> p.getStatus() == Status.ENTREGUE)
                .map(p -> new PedidoResponseUC6(p))
                .collect(Collectors.toList());

        return pedidosResponse;
    }
}