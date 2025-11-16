package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ItensEstoqueRepository;


public class ItensEstoqueRepositoryJDBC implements ItensEstoqueRepository{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ItensEstoqueRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getQuantidade (long ingredienteId){
        String sql = "SELECT quantidade FROM itensEstoque WHERE ingrediente_id = ?";
        Integer quantidade = jdbcTemplate.queryForObject(sql, Integer.class, ingredienteId);
        return quantidade != null ? quantidade : 0;
    }
    

    @Override
    public void atualizaQuantidade (long ingrediente_id, int quantidade){
        String sql = "UPDATE itensEstoque SET quantidade = ? WHERE ingrediente_id = ?";
        jdbcTemplate.update(sql, quantidade, ingrediente_id);
    }
}