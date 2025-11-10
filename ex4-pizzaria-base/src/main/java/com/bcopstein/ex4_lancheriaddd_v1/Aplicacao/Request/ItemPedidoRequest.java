package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request;

public class ItemPedidoRequest {
    private long produtoId;
    private int quantidade;

    public long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(long produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
