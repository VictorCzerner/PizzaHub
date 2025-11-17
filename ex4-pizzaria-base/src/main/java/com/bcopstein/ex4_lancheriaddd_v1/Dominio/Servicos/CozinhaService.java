package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;
    private final PedidoService pedidoService;

    private ScheduledExecutorService scheduler;

    public CozinhaService(PedidoService pedidoService) {
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.pedidoService = pedidoService;
        
    }

    private synchronized void colocaEmPreparacao(Pedido pedido){
        pedido.setStatus(Pedido.Status.PREPARACAO);
        pedidoService.atualizaPedido(pedido);
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: "+pedido);
        // Agenda pedidoPronto para ser chamado em 2 segundos
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    public synchronized void chegadaDePedido(Pedido p) {
        filaEntrada.add(p);
        System.out.println("Pedido na fila de entrada: "+p);
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoService.atualizaPedido(emPreparacao);
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saída: " + emPreparacao);

        // Quando o pedido estiver pronto, agenda a entrega
        scheduler.schedule(() -> iniciarEntrega(emPreparacao), 5, TimeUnit.SECONDS);

        emPreparacao = null;
        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 5, TimeUnit.SECONDS);
        }
    }

    private void iniciarEntrega(Pedido pedido) {
        try {
            pedido.setStatus(Pedido.Status.TRANSPORTE);
            pedidoService.atualizaPedido(pedido);
            System.out.println("Pedido em transporte: " + pedido);

            scheduler.schedule(() -> concluirEntrega(pedido), 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void concluirEntrega(Pedido pedido) {
        pedido.setStatus(Pedido.Status.ENTREGUE);
        pedidoService.atualizaPedido(pedido);
        System.out.println("Pedido entregue: " + pedido);
    }

}
