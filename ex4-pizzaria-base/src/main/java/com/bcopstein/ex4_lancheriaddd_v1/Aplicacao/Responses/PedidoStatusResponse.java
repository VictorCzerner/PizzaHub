package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoStatusResponse {
    private long id;
    private String clienteCpf;
    private Pedido.Status status;
    private double valor;
    private double valorCobrado;
    private double impostos;
    private double desconto;

    // Construtor que recebe o Pedido e extrai os dados
    public PedidoStatusResponse(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteCpf = pedido.getCliente().getCpf();
        this.status = pedido.getStatus();
        this.valor = pedido.getValor();
        this.valorCobrado = pedido.getValorCobrado();
        this.impostos = pedido.getImpostos();
        this.desconto = pedido.getDesconto();
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public Pedido.Status getStatus() {
        return status;
    }

    public double getValor() {
        return valor;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public double getImpostos() {
        return impostos;
    }

    public double getDesconto() {
        return desconto;
    }

    // Setter opcional (caso precise serializar ou atualizar algo manualmente)
    public void setStatus(Pedido.Status status) {
        this.status = status;
    }
}
