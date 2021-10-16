package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;
import com.grupo4.interfaces.GeradorRelatorioDiretoria;

public class Diretor extends Funcionario implements GeradorRelatorioDiretoria {
    private static final Cargo cargo = Cargo.DIRETOR;

    public Diretor(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    public static Cargo getCargo() {
        return cargo;
    }

}
