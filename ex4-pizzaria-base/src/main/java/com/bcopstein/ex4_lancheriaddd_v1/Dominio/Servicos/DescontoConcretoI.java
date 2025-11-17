package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface DescontoConcretoI {
    public double aplicarDesconto(Cliente cliente, Pedido pedido, double percentualDesconto);
    public String getNome();
}