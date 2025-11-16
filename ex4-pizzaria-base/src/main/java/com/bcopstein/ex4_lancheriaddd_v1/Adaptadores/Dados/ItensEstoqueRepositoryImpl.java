package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ItensEstoqueRepository;


import java.util.Optional;

@Repository
public class ItensEstoqueRepositoryImpl implements ItensEstoqueRepository {

    private final ItemEstoqueJPARepository jpaRepository;

    @Autowired
    public ItensEstoqueRepositoryImpl(ItemEstoqueJPARepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public int getQuantidade(long ingredienteId) {
        return jpaRepository.findById(ingredienteId)
                .map(ItemEstoqueBD::getQuantidade)
                .orElse(0);
    }

    @Override
    @Transactional
    public void atualizaQuantidade(long ingredienteId, int novaQuantidade) {
        Optional<ItemEstoqueBD> itemOpt = jpaRepository.findById(ingredienteId);

        if (itemOpt.isEmpty()) {
            // 🔒 Bloqueia qualquer tentativa de inserir novo item
            throw new RuntimeException(
                "Erro: tentativa de atualizar ingrediente inexistente no estoque (ID: " + ingredienteId + ")"
            );
        }

        ItemEstoqueBD item = itemOpt.get();
        item.setQuantidade(novaQuantidade);

        // ✅ Aqui o Hibernate vai gerar UPDATE, não INSERT
        jpaRepository.save(item);
    }
}
