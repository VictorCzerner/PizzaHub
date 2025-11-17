package com.bcopstein.ex4_lancheriaddd_v1.Infraestrutura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class ClienteUserDetailsService implements UserDetailsService {
    
    @Autowired
    private ClienteRepository clienteRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura cliente pelo e-mail (é o username no Basic Auth)
        Cliente cliente = clienteRepo.buscaPorCPF(username);
        if (cliente == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        // Retorna um objeto que o Spring Security entende
        return User.builder()
                .username(cliente.getEmail())
                .password(cliente.getSenha())
                .roles(cliente.getRole().name()) // “CLIENTE” ou “MASTER”
                .build();
    }
}
