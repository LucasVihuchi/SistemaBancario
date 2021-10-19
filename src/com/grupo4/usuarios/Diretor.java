package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;
import com.grupo4.interfaces.GeradorRelatorioDiretoria;

/** Classe para objetos do tipo Diretor, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Funcionario como superclasse.
 */
public class Diretor extends Funcionario implements GeradorRelatorioDiretoria {
    private static final Cargo cargo = Cargo.DIRETOR;

    /** Construtor para instanciar novo Gerente durante o fluxo da aplicação.
     *
     * @param nomeExt nome do diretor
     * @param cpfExt CPF do diretor
     * @param senhaExt senha do diretor
     */
    public Diretor(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    public static Cargo getCargo() {
        return cargo;
    }

}
