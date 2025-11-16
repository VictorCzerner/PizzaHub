package com.bcopstein.ex4_lancheriaddd_v1.Infraestrutura;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;

@Configuration
public class InicializaMaster {

    @Bean
    CommandLineRunner initMaster(ClienteRepository clienteRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            String masterCpf = "0000"; // CPF do cliente master
            if (clienteRepo.buscaPorCPF(masterCpf) == null) {
                Cliente master = new Cliente(
                    masterCpf,
                    "Master",
                    "000000000",
                    "Pizza Hub - Sede",
                    "master@pizzahub.com",
                    encoder.encode("master123"), // senha criptografada
                    Role.MASTER
                );
                clienteRepo.adicionaCliente(master);
                System.out.println("Usuário master criado!");
            }
        };
    }
}
