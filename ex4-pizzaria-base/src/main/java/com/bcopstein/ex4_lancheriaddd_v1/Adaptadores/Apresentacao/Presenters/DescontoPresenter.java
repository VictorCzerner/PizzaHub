package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.DescontoResponse;

public class DescontoPresenter {
    private int id;
    private String nome_desconto;
    private double percentagem_desconto;

    public DescontoPresenter(DescontoResponse descontoResponse){
        this.id = descontoResponse.getId();
        this.nome_desconto = descontoResponse.getNome_desconto();
        this.percentagem_desconto = descontoResponse.getPercentagem_desconto();
    }

    public int getId() {
        return id;
    }

    public String getNome_desconto() {
        return nome_desconto;
    }

    public double getPercentagem_desconto() {
        return percentagem_desconto;
    }
}