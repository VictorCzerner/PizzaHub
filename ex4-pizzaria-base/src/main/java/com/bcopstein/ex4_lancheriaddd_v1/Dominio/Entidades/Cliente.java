package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Cliente {
    private String cpf;
    private String nome;
    private String celular;
    private String endereco;
    private String email;
    private String senha;
    private Role role;

    public Cliente(String cpf, String nome, String celular, String endereco, String email, String senha, Role role ) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
        this.senha = senha;
        this.role = role;   
    }

    public Cliente() {
    }

    public Cliente(String cpf) {
        this.cpf = cpf;
    }

    public Role getRole() { return role; }   
    public String getSenha() { return senha; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public String getEndereco() { return endereco; }
    public String getEmail() { return email; }
}
