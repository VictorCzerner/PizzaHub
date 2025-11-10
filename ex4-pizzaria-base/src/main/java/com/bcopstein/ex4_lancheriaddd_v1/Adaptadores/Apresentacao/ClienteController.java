package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@RestController
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/cliente/{cpf}")
    public Cliente buscaClientePorCpf(@PathVariable String cpf) {
        Cliente cliente = clienteService.buscaPorCPF(cpf);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado para o CPF: " + cpf);
        }
        return cliente;
    }
}
