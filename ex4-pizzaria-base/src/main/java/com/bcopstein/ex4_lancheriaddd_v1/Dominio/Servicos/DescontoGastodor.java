package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class DescontoGastodor implements DescontoConcretoI{
    private final PedidoRepository pedidoRepository;
    private final String nome;

    @Autowired
    public DescontoGastodor(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
        this.nome =  "DESCONTO_GASTADOR";
    }

    public double aplicarDesconto(Cliente cliente, Pedido pedido, double percentagemDesconto) {
        double valorGasto30Dias = pedidoRepository.valorGastoUltimos30Dias(cliente.getCpf());
        double valorPedido = pedido.getValor();
        // Trata o erro mais comum
        if (valorPedido < 0) {
            throw new IllegalArgumentException("Valor base inválido para cálculo de desconto.");
        }

        if (valorGasto30Dias >= 500) {
            return valorPedido * (1 - percentagemDesconto);
        }
        else{
            return valorPedido;
        }
    }

    public String getNome(){
        return nome;
    }



}
