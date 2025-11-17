package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Repository
public class DescontosRepositoryJDBC implements DescontosRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DescontosRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean decideDescontoAtivo(long id) {
        // Verifica se o id existe antes de alterar qualquer registro
        String sqlExiste = "SELECT COUNT(*) FROM descontos WHERE id = ?";
        // O integer.class é para converter o resultado de sqlExiste para Inteiro
        // Se ele existe, o valo será = 1
        Integer descontoExiste = jdbcTemplate.queryForObject(sqlExiste, Integer.class, id);
        if (descontoExiste == null || descontoExiste == 0) {
            // id não encontrado — não altera nada
            return false;

        }

        // Primeiro, desativa todos os descontos
        String sqlDesativaTodos = "UPDATE descontos SET desconto_aplicado = false";
        jdbcTemplate.update(sqlDesativaTodos);

        // Em seguida, ativa apenas o desconto com o id informado
        String sqlAtiva = "UPDATE descontos SET desconto_aplicado = true WHERE id = ?";
        int linhasAfetadas = jdbcTemplate.update(sqlAtiva, id);

        // Retorna true se o id informado foi ativado
        return linhasAfetadas > 0;
    }

    @Override
    public List<Desconto> listaTodos() {
        String sql = "SELECT id, nome_desconto, percentual, desconto_aplicado FROM descontos ORDER BY id";

        // Pega cada linha da respoa sql e, através de uma função lambda, trasnforma os
        // objetos
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final int id = rs.getInt("id");
            final String nome = rs.getString("nome_desconto");
            final double percentual = rs.getDouble("percentual");
            final boolean ativo = rs.getBoolean("desconto_aplicado");

            return new Desconto(id, nome, percentual, ativo);
        });
    }

    @Override
    public double percentagemDesconto(String nome_desconto) {
        String sql = "SELECT percentual FROM descontos WHERE nome_desconto = ?";
        Number resultado = jdbcTemplate.queryForObject(sql, Number.class, nome_desconto);
        return resultado == null ? 0.0 : resultado.doubleValue();
    }

    @Override
    public Desconto retornaDescontoAtivo() {
        String sql = """
                    SELECT id, nome_desconto, percentual, desconto_aplicado
                    FROM descontos
                    WHERE desconto_aplicado = true
                    LIMIT 1
                """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new Desconto(
                            rs.getInt("id"),
                            rs.getString("nome_desconto"),
                            rs.getDouble("percentual"),
                            rs.getBoolean("desconto_aplicado")));
        } catch (EmptyResultDataAccessException e) {
            // Nenhum desconto ativo encontrado → retorna padrão
            return new Desconto(-1, "NENHUM_APLICADO", 0.0, true);
        }
    }

}