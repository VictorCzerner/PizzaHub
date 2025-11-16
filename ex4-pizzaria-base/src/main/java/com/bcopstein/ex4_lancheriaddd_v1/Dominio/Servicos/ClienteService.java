package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Request.ClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente buscaPorCPF(String cpf) {
        Cliente cliente = clienteRepository.buscaPorCPF(cpf);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado para o CPF: " + cpf);
        }
        return cliente;
    }

    public boolean adicionaCliente(ClienteRequest req) {
        
        if (clienteRepository.buscaPorCPF(req.getCpf()) != null) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF.");
        }

        String senhaHash = passwordEncoder.encode(req.getSenha());

        Cliente novo = new Cliente(
            req.getCpf(),
            req.getNome(),
            req.getCelular(),
            req.getEndereco(),
            req.getEmail(),
            senhaHash,
            Role.CLIENTE              
        );

        return clienteRepository.adicionaCliente(novo);
    }
}
