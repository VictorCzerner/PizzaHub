package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClienteRepositoryJDBC implements ClienteRepository {
private final JdbcTemplate jdbcTemplate;

@Autowired
public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
this.jdbcTemplate = jdbcTemplate;
}

    private static class ClienteRowMapper implements RowMapper {
        @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Cliente(
            rs.getString("cpf"),
            rs.getString("nome")
            );
        }
    }

    @Override
    public boolean adicionaCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (cpf, nome, celular, endereco, email, pedidos) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            int linhasAfetadas = jdbcTemplate.update(sql, cliente.getCpf(), cliente.getNome());
            return linhasAfetadas > 0;
        } catch (DataAccessException e) {
            // Pode acontecer se o cliente já existe (chave duplicada, etc.)
            System.err.println("Erro ao adicionar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente buscaPorCPF(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        List clientes = jdbcTemplate.query(sql, new ClienteRowMapper(), cpf);
        return clientes.isEmpty() ? null : clientes.get(0);
    }
}