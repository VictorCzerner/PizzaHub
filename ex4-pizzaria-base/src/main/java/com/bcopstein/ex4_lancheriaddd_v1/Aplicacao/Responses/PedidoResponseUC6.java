package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponseUC6 {
    private Pedido pedido;

    public PedidoResponseUC6(Pedido pedido){
        this.pedido = pedido;
    }
    Pedido getPedido(){return this.pedido;}
}