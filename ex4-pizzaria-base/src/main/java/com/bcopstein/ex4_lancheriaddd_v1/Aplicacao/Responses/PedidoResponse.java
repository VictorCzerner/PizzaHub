package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoResponse {
    private long id;
    private String clienteCpf;
    private LocalDateTime dataHoraPagamento;
    private String status;
    private double valor;
    private double impostos;
    private double desconto;
    private double valorCobrado;
    private List<ItemResponse> itens;
    private List<ItemResponse> itensEmFalta;

    public PedidoResponse(Pedido pedido, List<ItemResponse> itensEmFalta) {
        this.id = pedido.getId();
        this.clienteCpf = pedido.getCliente().getCpf();
        this.dataHoraPagamento = pedido.getDataHoraPagamento();
        this.status = pedido.getStatus().name();
        this.valor = pedido.getValor();
        this.impostos = pedido.getImpostos();
        this.desconto = pedido.getDesconto();
        this.valorCobrado = pedido.getValorCobrado();
        this.itens = pedido.getItens().stream()
                .map(i -> new ItemResponse(i.getItem().getDescricao(), i.getQuantidade()))
                .collect(Collectors.toList());
        this.itensEmFalta = itensEmFalta;
    }

    public static class ItemResponse {
        private String descricao;
        private int quantidade;

        public ItemResponse(String descricao, int quantidade) {
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

    public long getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
    public String getStatus() { return status; }
    public double getValor() { return valor; }
    public double getImpostos() { return impostos; }
    public double getDesconto() { return desconto; }
    public double getValorCobrado() { return valorCobrado; }
    public List<ItemResponse> getItens() { return itens; }
    public List<ItemResponse> getItensEmFalta() { return itensEmFalta; }
}
