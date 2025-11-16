package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ItensEstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItensEstoqueService {

    private final ItensEstoqueRepository itensEstoqueRepository;

    @Autowired
    public ItensEstoqueService(ItensEstoqueRepository itensEstoqueRepository) {
        this.itensEstoqueRepository = itensEstoqueRepository;
    }


    public int consultarQuantidade(long ingredienteId) {
        return itensEstoqueRepository.getQuantidade(ingredienteId);
    }


    public void atualizarQuantidade(long ingredienteId, int novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        itensEstoqueRepository.atualizaQuantidade(ingredienteId, novaQuantidade);
    }

    public void reduzirEstoque(Ingrediente ingrediente, int quantidade) {
        int atual = itensEstoqueRepository.getQuantidade(ingrediente.getId());

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a reduzir deve ser positiva.");
        }

        if (atual < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para o ingrediente: " + ingrediente.getDescricao());
        }

        int novaQuantidade = atual - quantidade;
        itensEstoqueRepository.atualizaQuantidade(ingrediente.getId(), novaQuantidade);
    }

    public void adicionarEstoque(Ingrediente ingrediente, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a adicionar deve ser positiva.");
        }

        int atual = itensEstoqueRepository.getQuantidade(ingrediente.getId());
        int novaQuantidade = atual + quantidade;
        itensEstoqueRepository.atualizaQuantidade(ingrediente.getId(), novaQuantidade);
    }

    public ItemEstoque obterItemEstoque(Ingrediente ingrediente) {
        int quantidadeAtual = itensEstoqueRepository.getQuantidade(ingrediente.getId());
        return new ItemEstoque(ingrediente, quantidadeAtual);
    }
}
