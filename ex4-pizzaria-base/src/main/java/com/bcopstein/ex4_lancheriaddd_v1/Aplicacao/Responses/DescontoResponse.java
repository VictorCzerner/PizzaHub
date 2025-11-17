package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

public class DescontoResponse {
    private int id;
    private String nome_desconto;
    private double percentagem_desconto;

    public DescontoResponse(Desconto desconto){
        this.id = desconto.getId();
        // Tranforma "DESCONTO_NOME" em "Desconto Nome"
        this.nome_desconto = Arrays.stream(desconto.getNome().split("_"))
            .map(p -> p.substring(0,1).toUpperCase() + p.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
        this.percentagem_desconto = desconto.getDesconto();
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