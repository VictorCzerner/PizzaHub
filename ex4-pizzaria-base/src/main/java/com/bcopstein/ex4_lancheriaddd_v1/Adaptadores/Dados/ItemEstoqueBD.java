package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import jakarta.persistence.*;

@Entity
@Table(name = "itensestoque")
public class ItemEstoqueBD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingrediente_id", nullable = false)
    private Long ingredienteId;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    public ItemEstoqueBD() {}

    public ItemEstoqueBD(Long id, Long ingredienteId, int quantidade) {
        this.id = id;
        this.ingredienteId = ingredienteId;
        this.quantidade = quantidade;
    }

    public Long getId() { return id; }
    public Long getIngredienteId() { return ingredienteId; }
    public int getQuantidade() { return quantidade; }

    public void setId(Long id) { this.id = id; }
    public void setIngredienteId(Long ingredienteId) { this.ingredienteId = ingredienteId; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    // ---------------- MÉTODOS DE CONVERSÃO ----------------

    public static ItemEstoque toItemEstoque(ItemEstoqueBD bd, Ingrediente ingrediente) {
        return new ItemEstoque(ingrediente, bd.getQuantidade());
    }

    public static ItemEstoqueBD fromItemEstoque(ItemEstoque item) {
        return new ItemEstoqueBD(null, item.getIngrediente().getId(), item.getQuantidade());
    }
}
