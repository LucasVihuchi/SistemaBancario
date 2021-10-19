package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;

/** Classe abstrata para objetos do tipo Funcionario, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Usuario como superclasse.
 */
public abstract class Funcionario extends Usuario{
    private final static String senhaAdmin = "admin123";
    protected static Cargo cargo;

    /** Construtor para instanciar novo Funcionario durante o fluxo da aplicação.
     *
     * @param nomeExt nome do funcionário
     * @param cpfExt CPF do funcionário
     * @param senhaExt senha do funcionário
     */
    public Funcionario(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    public static Cargo getCargo(){
        return Funcionario.cargo;
    }

    public static String getSenhaAdmin() {
        return senhaAdmin;
    }
}
