package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Repository
public class ClienteRepositoryJDBC implements ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean adicionaCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES (?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(
            sql,
            cliente.getCpf(),
            cliente.getNome(),
            cliente.getCelular(),
            cliente.getEndereco(),
            cliente.getEmail()
        );
        return rows > 0;
    }

    @Override
    public Cliente buscaPorCPF(String cpf) {
        String sql = "SELECT cpf, nome, celular, endereco, email FROM clientes WHERE cpf = ?";
        try {
            return jdbcTemplate.query(
                sql,
                ps -> ps.setString(1, cpf),
                rs -> {
                    if (rs.next()) {
                        return new Cliente(
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("celular"),
                            rs.getString("endereco"),
                            rs.getString("email")
                        );
                    }
                    return null;
                }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
