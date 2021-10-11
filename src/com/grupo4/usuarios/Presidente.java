package com.grupo4.usuarios;

import com.grupo4.enums.Cargo;
import com.grupo4.interfaces.GeradorRelatorioDiretoria;

public class Presidente extends Funcionario implements GeradorRelatorioDiretoria{
    static {
        Presidente.cargo = Cargo.PRESIDENTE;
    }

    public Presidente(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    public void geraRelatorioCapitalBanco() {
        // TODO Implementar rotina de geração de relatorio de capital total do banco
    }
}
