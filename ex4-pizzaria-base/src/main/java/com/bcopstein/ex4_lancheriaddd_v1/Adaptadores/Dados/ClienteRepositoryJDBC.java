package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;

@Repository
public class ClienteRepositoryJDBC implements ClienteRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean adicionaCliente(Cliente cliente) {
    String sql = "INSERT INTO clientes (cpf, nome, celular, endereco, email, senha, role) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

    int rows = jdbcTemplate.update(
        sql,
        cliente.getCpf(),
        cliente.getNome(),
        cliente.getCelular(),
        cliente.getEndereco(),
        cliente.getEmail(),
        cliente.getSenha(),        
        cliente.getRole().name()   
    );
    return rows > 0;
}

    @Override
    public Cliente buscaPorCPF(String cpf) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha, role FROM clientes WHERE cpf = ?";

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
                        rs.getString("email"),
                        rs.getString("senha"),             
                        Role.valueOf(rs.getString("role"))  
                    );
                }
                return null;
            }
        );
    }
}
