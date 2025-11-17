package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;

@Component
public class DecideDescontoAtivoUC {
    private DescontoService descontoService;

    @Autowired
    public DecideDescontoAtivoUC(DescontoService descontoService){
        this.descontoService = descontoService;
    }

    public boolean executar(long id){
        boolean escolhaFeita = descontoService.decideDescontoAtivo(id);
        return escolhaFeita;
    } 
}