package com.Grupo01.PizzaHUB.Adaptadores.Apresentacao;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Grupo01.PizzaHUB.Adaptadores.Apresentacao.Presenters.DescontoPresenter;

import com.Grupo01.PizzaHUB.Aplicacao.RecuperaListaDescontosUC;

import com.Grupo01.PizzaHUB.Aplicacao.Responses.DescontoResponse;

@RestController
public class DescontosController {

    public final RecuperaListaDescontosUC recuperaListaDescontosUC;

    @Autowired
    public DescontosController(RecuperaListaDescontosUC recuperaListaDescontosUC) {
        this.recuperaListaDescontosUC = recuperaListaDescontosUC;
    }

    // Mostra ao usuário uma lista com todos os descontos disponíveis
    @GetMapping("/descontos_disponiveis")
    @CrossOrigin("*")
    public List<DescontoPresenter> consultaDescontos() {
        List<DescontoResponse> listaDescontosResponse = recuperaListaDescontosUC.executar();
        List<DescontoPresenter> listaDescontosPresenter = new ArrayList<>();

        listaDescontosResponse.forEach(response -> listaDescontosPresenter.add(new DescontoPresenter(response)));
        return listaDescontosPresenter;
    }

}
