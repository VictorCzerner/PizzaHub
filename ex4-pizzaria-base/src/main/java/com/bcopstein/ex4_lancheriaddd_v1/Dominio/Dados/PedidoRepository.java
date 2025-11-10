package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.sql.Timestamp;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido criaPedido (Pedido pedido);
    Pedido buscaPorId(long id);
    boolean atualiza (Pedido pedido);
    int quantidadePedidosUltimos20Dias(String clienteCpf);
    boolean mudaStatus (long pedidoId, Pedido.Status status);
    List<Pedido> listaPorIntervalo(Timestamp dataInicio, Timestamp dataFinal);
}