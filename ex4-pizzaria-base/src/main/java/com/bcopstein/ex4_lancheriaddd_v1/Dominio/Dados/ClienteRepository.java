package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteRepository {

    boolean adicionaCliente(Cliente cliente);
    Cliente buscaPorCPF(String CPF);

    
}
