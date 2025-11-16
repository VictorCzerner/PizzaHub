package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;

public class Cardapio {
    private CabecalhoCardapio cabecalhoCardapio;
    private List<Produto> produtos;
    private boolean ativo;

    public Cardapio(long id, String titulo, List<Produto> produtos, boolean ativo) {
        this.cabecalhoCardapio = new CabecalhoCardapio(id,titulo);
        this.produtos = produtos;
        this.ativo = ativo;
    }

    public long getId() { return cabecalhoCardapio.id(); }
    public String getTitulo(){ return cabecalhoCardapio.titulo(); }
    public CabecalhoCardapio getCabecalhoCardapio(){ return cabecalhoCardapio; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos){this.produtos = produtos;}
    public boolean getAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
