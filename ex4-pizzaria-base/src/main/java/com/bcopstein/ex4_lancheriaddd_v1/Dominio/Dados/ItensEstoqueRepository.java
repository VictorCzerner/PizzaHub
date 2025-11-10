package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

public interface ItensEstoqueRepository{
    int getQuantidade (long ingredienteId);
    void atualizaQuantidade (long ingrediente_id, int quantidade);
}