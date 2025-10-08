package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido criaPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, dataHoraPagamento, status_pedido, valor, impostos, descontos, valorCobrado) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlPedido,
            pedido.getCliente().getCpf(),
            Timestamp.valueOf(pedido.getDataHoraPagamento()),
            pedido.getStatus().name(),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado()
        );

        // depois de inserir o pedido, você precisaria buscar o id gerado
        // aqui, vou supor que você vai buscar pelo último id (pode variar conforme banco)
        Long pedidoId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM pedidos", Long.class);
        pedido.setId(pedidoId);

        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItem,
                pedidoId,
                item.getItem().getId(),
                item.getQuantidade()
            );
        }

        return pedido;
    }

    @Override
    public boolean atualiza(Pedido pedido) {
        String sql = "UPDATE pedidos SET " +
                    "status_pedido = ?, " +
                    "valor = ?, " +
                    "impostos = ?, " +
                    "desconto = ?, " +
                    "valorCobrado = ? " +
                    "WHERE id = ?";

        int linhasAfetadas = jdbcTemplate.update(sql,
            pedido.getStatus().name(),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado(),
            pedido.getId()
        );

        return linhasAfetadas > 0;
    }

   @Override
    public boolean mudaStatus(long pedidoId, Pedido.Status status) {
        String sql = "UPDATE pedidos SET status_pedido = ? WHERE id = ?";
        int linhasAfetadas = jdbcTemplate.update(sql, status.name(), pedidoId);
        return linhasAfetadas > 0; // true se atualizou, false se não
    }

    @Override
    public int quantidadePedidosUltimos20Dias(String clienteCpf) {
    String sql = "SELECT COUNT(*) FROM pedidos " +
                 "WHERE cliente_cpf = ? AND dataHoraPagamento >= CURRENT_DATE - INTERVAL '20' DAY";
    
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clienteCpf);
    return count != null ? count : 0;
    }

    @Override
    public Pedido buscaPorId(long id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        List<Pedido> pedidos = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToPedido(rs), id);

        return pedidos.isEmpty() ? null : pedidos.get(0);
    }

    private Pedido mapRowToPedido(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String clienteCpf = rs.getString("cliente_cpf");
        Cliente cliente = new Cliente(clienteCpf);

        LocalDateTime dataHoraPagamento = rs.getTimestamp("dataHoraPagamento").toLocalDateTime();
        Pedido.Status status = Pedido.Status.valueOf(rs.getString("status_pedido"));
        double valor = rs.getDouble("valor");
        double impostos = rs.getDouble("impostos");
        double desconto = rs.getDouble("descontos");
        double valorCobrado = rs.getDouble("valorCobrado");

        // Como não temos a lista de itens salva, por enquanto passamos uma lista vazia
        return new Pedido(id, cliente, dataHoraPagamento, List.of(), status, valor, impostos, desconto, valorCobrado);
    }

    @Override
    public List<Pedido> listaPorIntervalo(Timestamp dataInicio, Timestamp dataFinal) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
            SELECT id, cliente_cpf, dataHoraPagamento, status_pedido,
                valor, impostos, descontos, valorCobrado
            FROM pedidos
            WHERE dataHoraPagamento BETWEEN ? AND ?
            ORDER BY dataHoraPagamento
        """;

        // Exemplo: usando DataSource injetado
        try (Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, dataInicio);
            stmt.setTimestamp(2, dataFinal);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getLong("id"));
                    pedido.setClienteCpf(rs.getLong("cliente_cpf")); // ou getString se for VARCHAR

                    Timestamp timeStamp = rs.getTimestamp("dataHoraPagamento");
                    if (timeStamp != null) {
                        pedido.setDataHoraPagamento(timeStamp.toLocalDateTime());
                    }

                    pedido.setStatusPedido(rs.getString("status_pedido"));
                    pedido.setValor(rs.getBigDecimal("valor"));
                    pedido.setImpostos(rs.getBigDecimal("impostos"));
                    pedido.setDescontos(rs.getBigDecimal("descontos"));
                    pedido.setValorCobrado(rs.getBigDecimal("valorCobrado"));

                    pedidos.add(pedido);
                }
            }
        }

        return pedidos;
}


}