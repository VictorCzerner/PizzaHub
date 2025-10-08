package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponseUC6;

import java.util.List;

public class ListaPedidoPresenter {
    private List<PedidoResponseUC6> pedidosResponseUC6;

    public ListaPedidoPresenter (List<PedidoResponseUC6> pedidosResponseUC6){
        this.pedidosResponseUC6 = pedidosResponseUC6;
    }
}