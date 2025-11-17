package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.DescontoPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DecideDescontoAtivoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaListaDescontosUC;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.DescontoResponse;

@RestController
@RequestMapping("/descontos")
public class DescontosController {

    public final RecuperaListaDescontosUC recuperaListaDescontosUC;
    private final DecideDescontoAtivoUC decideDescontoAtivoUC;

    @Autowired
    public DescontosController(RecuperaListaDescontosUC recuperaListaDescontosUC, DecideDescontoAtivoUC decideDescontoAtivoUC) {
        this.recuperaListaDescontosUC = recuperaListaDescontosUC;
        this.decideDescontoAtivoUC = decideDescontoAtivoUC;
    }

    // Mostra ao usuário uma lista com todos os descontos disponíveis
    @GetMapping("/descontosDisponiveis")
    @CrossOrigin("*")
    public ResponseEntity<List<DescontoPresenter>> consultaDescontos() {
        List<DescontoResponse> listaDescontosResponse = recuperaListaDescontosUC.executar();
        List<DescontoPresenter> listaDescontosPresenter = new ArrayList<>();

        listaDescontosResponse.forEach(response -> listaDescontosPresenter.add(new DescontoPresenter(response)));
        return ResponseEntity.ok(listaDescontosPresenter);
    }

    @GetMapping("/DecideDesconto/{id}")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> decideDescontoAplicado(@PathVariable long id) {
        boolean descontoAtivoAlterado = decideDescontoAtivoUC.executar(id);
        return ResponseEntity.ok(descontoAtivoAlterado);
    }

}