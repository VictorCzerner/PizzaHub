package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoStatusPresenter {
    private long id;
    
    private Pedido.Status status;

    public PedidoStatusPresenter(long id, Pedido.Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public Pedido.Status getStatus() {
        return status;
    }
}
