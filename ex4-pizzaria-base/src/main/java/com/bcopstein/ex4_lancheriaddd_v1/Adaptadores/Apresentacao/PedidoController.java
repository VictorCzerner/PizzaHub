package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.ListaPedidoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoStatusPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AprovaPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelaPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ConsultaStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListaPedidosPorIntervaloUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoStatusResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final AprovaPedidoUC aprovaPedidoUC;
    private final ConsultaStatusPedidoUC consultaStatusPedidoUC;
    private final CancelaPedidoUC cancelaPedidoUC;
    private final ListaPedidosPorIntervaloUC listaPedidosPorIntervaloUC;

    @Autowired
    public PedidoController(AprovaPedidoUC aprovaPedidoUC, ConsultaStatusPedidoUC consultaStatusPedidoUC, CancelaPedidoUC cancelaPedidoUC, ListaPedidosPorIntervaloUC listaPedidosPorIntervaloUC) {
        this.aprovaPedidoUC = aprovaPedidoUC;
        this.consultaStatusPedidoUC = consultaStatusPedidoUC;
        this.cancelaPedidoUC = cancelaPedidoUC;
        this.listaPedidosPorIntervaloUC = listaPedidosPorIntervaloUC;
    }

    @PostMapping("/submetePedido")
    @CrossOrigin("*")
    public ResponseEntity<PedidoPresenter> submetePedido(@RequestBody PedidoRequest pedidoRequest) {
        PedidoResponse response = aprovaPedidoUC.executar(pedidoRequest);
        PedidoPresenter presenter = new PedidoPresenter(response);
        return ResponseEntity.ok(presenter);
    }   

    @GetMapping("/status/{id}")
    @CrossOrigin("*")
    public ResponseEntity<PedidoStatusPresenter> consultaStatus(@PathVariable long id) {
        PedidoStatusResponse pedido = consultaStatusPedidoUC.executar(id);
        PedidoStatusPresenter presenter = new PedidoStatusPresenter(pedido.getId(), pedido.getStatus());
        return ResponseEntity.ok(presenter);
    }

    @GetMapping("/cancelaPedido/{id}")
    @CrossOrigin("*")
    public ResponseEntity<PedidoStatusPresenter> cancelaPedido(@PathVariable long id) {
        PedidoStatusResponse pedido = cancelaPedidoUC.executar(id);
        PedidoStatusPresenter presenter = new PedidoStatusPresenter(pedido.getId(), pedido.getStatus());
        return ResponseEntity.ok(presenter);
    }

    @GetMapping("/pedidosEntreguesEntre/{dataInicio}/{dataFinal}")
    @CrossOrigin("*")
    public ResponseEntity<ListaPedidoPresenter> listaPedidosEntreguesEntre (@PathVariable LocalDateTime dataInicio, 
    @PathVariable LocalDateTime dataFinal){
        ListaPedidoPresenter listaPedidos = new ListaPedidoPresenter(listaPedidosPorIntervaloUC.listaPedidosEntregueDatas(dataInicio, dataFinal));
        return ResponseEntity.ok(listaPedidos);
    }


}

