package com.Grupo01.PizzaHUB.Adaptadores.Dados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.Grupo01.PizzaHUB.Dominio.Dados.PedidoRepository;
import com.Grupo01.PizzaHUB.Dominio.Entidades.Cliente;
import com.Grupo01.PizzaHUB.Dominio.Entidades.ItemPedido;
import com.Grupo01.PizzaHUB.Dominio.Entidades.Pedido;

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

        // Verificar problema de valor nulo e concorrencia
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
                    "descontos = ?, " +
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
                    "WHERE cliente_cpf = ? AND dataHoraPagamento >= CURRENT_DATE - INTERVAL '20 days'";
        
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, clienteCpf);
        return count != null ? count : 0;
    }
    // Retorna o valor gasto nos últimos 30 dias
    public double valorGastoUltimos30Dias(String clienteCpf){
        String sql = "SELECT COALESCE(SUM(valorCobrado), 0) FROM pedidos " +
                     "WHERE cliente_cpf = ? AND dataHoraPagamento >= CURRENT_DATE - INTERVAL '30 days'";

        Number soma = jdbcTemplate.queryForObject(sql, Number.class, clienteCpf);
        return soma == null ? 0f : soma.doubleValue();
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
    public List<Pedido> listaPorIntervalo(Timestamp dataInicio, Timestamp dataFinal) {
        String sql = """
            SELECT id, cliente_cpf, dataHoraPagamento, status_pedido,
                valor, impostos, descontos, valorCobrado
            FROM pedidos
            WHERE dataHoraPagamento BETWEEN ? AND ?
            ORDER BY dataHoraPagamento
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapeiaParaPedido(rs), dataInicio, dataFinal);
    }

    private Pedido mapeiaParaPedido(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String clienteCpf = rs.getString("cliente_cpf");
        Cliente cliente = new Cliente(clienteCpf);

        LocalDateTime dataHoraPagamento = rs.getTimestamp("dataHoraPagamento").toLocalDateTime();
        Pedido.Status status = Pedido.Status.valueOf(rs.getString("status_pedido"));
        double valor = rs.getDouble("valor");
        double impostos = rs.getDouble("impostos");
        double desconto = rs.getDouble("descontos");
        double valorCobrado = rs.getDouble("valorCobrado");

        return new Pedido(id, cliente, dataHoraPagamento, List.of(), status, valor, impostos, desconto, valorCobrado);
    }



}
