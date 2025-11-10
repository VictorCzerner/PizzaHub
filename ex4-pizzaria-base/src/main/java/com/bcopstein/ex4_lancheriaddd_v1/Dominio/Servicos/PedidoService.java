package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ItensEstoqueRepository itensEstoqueRepository;
    private final ProdutosRepository produtoRepository;
    private final ImpostoService impostoService;
    private final DescontoService descontoService;

    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository,
            ItensEstoqueRepository itensEstoqueRepository,
            ProdutosRepository produtoRepository,
            ImpostoService impostoService,
            DescontoService descontoService) {

        this.pedidoRepository = pedidoRepository;
        this.itensEstoqueRepository = itensEstoqueRepository;
        this.produtoRepository = produtoRepository;
        this.impostoService = impostoService;
        this.descontoService = descontoService;
    }

    public Pedido criaPedido(PedidoRequest pedidoRequest) {
        // Cria cliente apenas com CPF (sem buscar no banco)
        Cliente cliente = new Cliente(pedidoRequest.getClienteCpf());

        // Monta os itens com base no produtoId e quantidade
        List<ItemPedido> itens = pedidoRequest.getItens().stream()
                .map(req -> {
                    Produto produto = produtoRepository.recuperaProdutoPorid(req.getProdutoId());
                    return new ItemPedido(produto, req.getQuantidade());
                })
                .collect(Collectors.toList());

        Pedido pedido = new Pedido(
                0L,
                cliente,
                LocalDateTime.now(),
                itens,
                Pedido.Status.NOVO,
                0, 0, 0, 0
        );

        return pedido;
    }

    public Pedido salvarPedido(Pedido pedido) {
        return pedidoRepository.criaPedido(pedido);
    }

    public boolean atualizaPedido(Pedido pedido){
        Pedido ped = buscaPorId(pedido.getId());
        if (ped != null) {
            pedidoRepository.atualiza(pedido);
            return true;
        }
        else{
            return false;
        }
        
    }

    

    public Pedido aprovaPedido(Pedido pedido) {
        if (verificaItens(pedido).isEmpty()) {
            double custoFinal = calculaCusto(pedido);
            pedido.setStatus(Pedido.Status.APROVADO);
            pedido.setValorCobrado(custoFinal);
            pedidoRepository.criaPedido(pedido);
            System.out.println("Pedido aprovado! Preço total: " + custoFinal);
        } else {
            System.out.println("Pedido negado! Ingredientes em falta:");
            verificaItens(pedido).forEach(item -> System.out.println("- " + item.getItem().getDescricao()));
            pedido.setStatus(Pedido.Status.CANCELADO);
        }

        return pedido;
    }

    /**
     * Verifica se há ingredientes em falta no estoque.
     */
    public List<ItemPedido> verificaItens(Pedido pedido) {
        List<ItemPedido> itensEmFalta = new ArrayList<>();

        for (ItemPedido itemPedido : pedido.getItens()) {
            Produto produto = itemPedido.getItem();
            Receita receita = produto.getReceita();

            for (Ingrediente ingrediente : receita.getIngredientes()) {
                int quantidadeNecessaria = itemPedido.getQuantidade();
                int quantidadeEstoque = itensEstoqueRepository.getQuantidade(ingrediente.getId());

                if (quantidadeEstoque < quantidadeNecessaria) {
                    itensEmFalta.add(itemPedido);
                }
            }
        }

        return itensEmFalta;
    }


    public double calculaCusto(Pedido pedido) {
        double valor = pedido.getItens().stream()
                .mapToDouble(item -> (item.getItem().getPreco() * item.getQuantidade()) / 100)
                .sum();

        pedido.setValor(valor);
        double valorCobrado = valor;

        int pedidosRecentes = pedidoRepository.quantidadePedidosUltimos20Dias(pedido.getCliente().getCpf());

        // aplica desconto via serviço
        double valorComDesconto = descontoService.aplicarDesconto(valorCobrado, pedidosRecentes);
        double percentualDesconto = descontoService.getPercentualDesconto(pedidosRecentes);
        pedido.setDesconto(percentualDesconto);
        

        double impostos = impostoService.calcularImpostos(valorComDesconto);
        pedido.setImpostos(impostos);

        valorCobrado = valorComDesconto + impostos;
        pedido.setValorCobrado(valorCobrado);

        return valorCobrado;
    }

    public Pedido buscaPorId(long id) {
        return pedidoRepository.buscaPorId(id);
    }

    public Pedido CancelaPedido(long id) {
        pedidoRepository.mudaStatus(id, Pedido.Status.CANCELADO);
        return pedidoRepository.buscaPorId(id);
    }

    public List<Pedido> listaPedidosEntreDatas(LocalDateTime dataInicio, LocalDateTime dataFinal){
        Timestamp tsInicio = Timestamp.valueOf(dataInicio);
        Timestamp tsFinal = Timestamp.valueOf(dataFinal);


        List<Pedido> pedidos = pedidoRepository.listaPorIntervalo(tsInicio, tsFinal);
        return pedidos;
    }

}
