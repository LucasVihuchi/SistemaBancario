package com.grupo4.usuarios;

/** Classe para objetos do tipo Cliente, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Usuario como superclasse.
 */
public class Cliente extends Usuario{
    /** Construtor para instanciar novo Cliente durante o fluxo da aplicação.
     *
     * @param nomeExt nome do cliente
     * @param cpfExt CPF do cliente
     * @param senhaExt senha do cliente para acessar as contas
     */
    public Cliente(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }
}
