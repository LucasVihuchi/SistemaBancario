package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;

public abstract class Funcionario extends Usuario{
    private final static String senhaAdmin = "admin123";
    protected static Cargo cargo;

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
