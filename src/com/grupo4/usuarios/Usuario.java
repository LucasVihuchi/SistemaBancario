package com.grupo4.usuarios;

import com.grupo4.exceptions.SenhaIncorretaException;


public abstract class Usuario implements Comparable {
    protected String nome;
    protected String cpf;
    protected String senha;

    public Usuario(String nomeExt, String cpfExt, String senhaExt) {
        this.nome = nomeExt;
        this.cpf = cpfExt;
        this.senha = senhaExt;
    }

    public void logar(String senhaInserida) throws SenhaIncorretaException{
        if(!this.senha.equals(senhaInserida)) {
            throw new SenhaIncorretaException();
        }
        System.out.println("Usuário logado com sucesso!");
    }

    public String getNome() {
        return this.nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getSenha() {
        return this.senha;
    }

    @Override
    public int compareTo(Object o) {
        return this.nome.compareToIgnoreCase(((Usuario) o).nome);
    }
}
