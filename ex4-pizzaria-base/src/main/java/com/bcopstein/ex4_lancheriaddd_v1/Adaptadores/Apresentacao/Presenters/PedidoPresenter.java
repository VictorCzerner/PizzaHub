package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import java.util.List;

public class PedidoPresenter {
    private long id;
    private String clienteCpf;
    private String status;
    private double valorCobrado;
    private List<ItemPresenter> itens;
    private List<ItemPresenter> itensEmFalta;

    public PedidoPresenter(PedidoResponse response) {
        this.id = response.getId();
        this.clienteCpf = response.getClienteCpf();
        this.status = response.getStatus();
        this.valorCobrado = response.getValorCobrado();

        if (response.getItens() != null) {
            this.itens = response.getItens().stream()
                    .map(i -> new ItemPresenter(i.getDescricao(), i.getQuantidade()))
                    .toList();
        }

        if (response.getItensEmFalta() != null) {
            this.itensEmFalta = response.getItensEmFalta().stream()
                    .map(i -> new ItemPresenter(i.getDescricao(), i.getQuantidade()))
                    .toList();
        }
    }


    public static class ItemPresenter {
        private String descricao;
        private int quantidade;

        public ItemPresenter(String descricao, int quantidade) {
            this.descricao = descricao;
            this.quantidade = quantidade;
        }

        public String getDescricao() {
            return descricao;
        }

        public int getQuantidade() {
            return quantidade;
        }
    }

    // Getters públicos (necessários para o JSON)
    public long getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public String getStatus() { return status; }
    public double getValorCobrado() { return valorCobrado; }
    public List<ItemPresenter> getItens() { return itens; }
    public List<ItemPresenter> getItensEmFalta() { return itensEmFalta; }
}
