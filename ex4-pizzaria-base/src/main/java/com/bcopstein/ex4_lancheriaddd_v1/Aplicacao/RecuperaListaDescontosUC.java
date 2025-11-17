package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.DescontoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;

@Component
public class RecuperaListaDescontosUC {
    private DescontoService descontoService;

    @Autowired
    public RecuperaListaDescontosUC(DescontoService descontoService){
        this.descontoService = descontoService;
    }

    public List<DescontoResponse> executar(){
        List<DescontoResponse> listaResponses = new ArrayList<>();
        List<Desconto> todosDescontos = descontoService.RecuperaTodosDescontos();

        todosDescontos.forEach(desconto -> listaResponses.add(new DescontoResponse(desconto)));
        return listaResponses;
    }
}