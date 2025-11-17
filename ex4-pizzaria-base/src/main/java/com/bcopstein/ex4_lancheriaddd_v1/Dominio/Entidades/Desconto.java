package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Desconto {
    private int id;
    private String nome;
    private double desconto;
    private boolean ativo;

    public Desconto(int id, String nome, double desconto, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.desconto = desconto;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}