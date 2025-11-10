package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    public enum Status {
        NOVO,
        APROVADO,
        CANCELADO,
        PAGO,
        AGUARDANDO,
        PREPARACAO,
        PRONTO,
        TRANSPORTE,
        ENTREGUE
    }

    private long id;
    private Cliente cliente;
    private LocalDateTime dataHoraPagamento;
    private List<ItemPedido> itens;
    private Status status;
    private double valor;
    private double impostos;
    private double desconto;
    private double valorCobrado;

    public Pedido(long id, Cliente cliente, LocalDateTime dataHoraPagamento, List<ItemPedido> itens,
            Pedido.Status status, double valor, double impostos, double desconto, double valorCobrado) {
        this.id = id;
        this.cliente = cliente;
        this.dataHoraPagamento = dataHoraPagamento;
        this.itens = itens;
        this.status = status;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setImpostos(double impostos) {
        this.impostos = impostos;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public double getImpostos() {
        return impostos;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }
}
