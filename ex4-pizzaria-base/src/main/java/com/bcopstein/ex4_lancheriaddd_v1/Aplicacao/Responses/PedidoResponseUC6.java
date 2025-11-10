package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponseUC6{
    private Pedido pedido;
    private long id;
    private String clienteCpf;
    private LocalDateTime dataHoraPagamento;
    private String status;
    private double valorCobrado;

    public PedidoResponseUC6(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteCpf = pedido.getCliente().getCpf();
        this.dataHoraPagamento = pedido.getDataHoraPagamento();
        this.status = pedido.getStatus().name();
        this.valorCobrado = pedido.getValorCobrado();
    }

    public long getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
    public String getStatus() { return status; }
    public double getValorCobrado() { return valorCobrado; }
}