package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;

public abstract class Funcionario extends Usuario{
    protected static Cargo cargo;

    public Funcionario(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    public static Cargo getCargo(){
        return Funcionario.cargo;
    }
}
