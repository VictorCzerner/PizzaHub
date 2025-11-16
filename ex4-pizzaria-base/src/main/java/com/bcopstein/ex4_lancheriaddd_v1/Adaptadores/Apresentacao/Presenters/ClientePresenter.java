package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Role;

public class ClientePresenter {

    private final String cpf;
    private final String nome;
    private final String celular;
    private final String endereco;
    private final String email;
    private final Role role;

    public ClientePresenter(Cliente cliente) {
        this.cpf = cliente.getCpf();
        this.nome = cliente.getNome();
        this.celular = cliente.getCelular();
        this.endereco = cliente.getEndereco();
        this.email = cliente.getEmail();
        this.role = cliente.getRole();
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public String getEndereco() { return endereco; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
