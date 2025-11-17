package com.bcopstein.ex4_lancheriaddd_v1.Infraestrutura;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ROTAS PÚBLICAS
                .requestMatchers("/clientes/**").permitAll()
                .requestMatchers("/cardapio/recuperaAtivo").permitAll()


                // ROTAS PROTEGIDAS
                .requestMatchers("/pedidos/**").hasAnyRole("CLIENTE", "MASTER")
                .requestMatchers("/cardapio/lista", "/cardapio/buscaPorId/**").hasRole("CLIENTE")
                .requestMatchers("/cardapio/**").hasRole("MASTER")
                .requestMatchers("/desconto/DecideDesconto").hasRole("MASTER")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}