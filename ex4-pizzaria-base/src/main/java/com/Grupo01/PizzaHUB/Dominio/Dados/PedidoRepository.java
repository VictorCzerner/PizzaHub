package com.Grupo01.PizzaHUB.Dominio.Dados;

import java.sql.Timestamp;
import java.util.List;

import com.Grupo01.PizzaHUB.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido criaPedido (Pedido pedido);
    Pedido buscaPorId(long id);
    boolean atualiza (Pedido pedido);
    int quantidadePedidosUltimos20Dias(String clienteCpf);
    float valorGastoUltimos30Dias(String clienteCpf);
    boolean mudaStatus (long pedidoId, Pedido.Status status);
    List<Pedido> listaPorIntervalo(Timestamp dataInicio, Timestamp dataFinal);
}