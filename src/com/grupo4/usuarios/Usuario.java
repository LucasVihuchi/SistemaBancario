package com.grupo4.usuarios;

import com.grupo4.exceptions.SenhaIncorretaException;

/** Classe abstrata para objetos do tipo Usuário, onde serão contidos, atributos e métodos para o mesmo.
 */
public abstract class Usuario implements Comparable<Usuario> {
    protected String nome;
    protected String cpf;
    protected String senha;

    /** Construtor para instanciar novo Usuario durante o fluxo da aplicação.
     *
     * @param nomeExt nome do usuário
     * @param cpfExt CPF do usuário
     * @param senhaExt senha do usuário para acessar as contas
     */
    public Usuario(String nomeExt, String cpfExt, String senhaExt) {
        this.nome = nomeExt;
        this.cpf = cpfExt;
        this.senha = senhaExt;
    }

    /** Método para realizar a autenticação do usuário.
     *
     * @param senhaInserida senha a ser comparada
     * @throws SenhaIncorretaException se a senha inserida não estiver incorreta
     */
    public void logar(String senhaInserida) throws SenhaIncorretaException{
        if(!this.senha.equals(senhaInserida)) {
            throw new SenhaIncorretaException();
        }
        System.out.println("\nUsuário logado com sucesso!");
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
    public int compareTo(Usuario o) {
        return this.nome.compareToIgnoreCase(((Usuario) o).nome);
    }
}
