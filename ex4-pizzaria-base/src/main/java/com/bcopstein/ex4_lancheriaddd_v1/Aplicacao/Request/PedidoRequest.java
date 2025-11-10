package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request;

import java.util.List;

public class PedidoRequest {
    private String clienteCpf;
    private List<ItemPedidoRequest> itens;

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}
