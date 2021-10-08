package com.grupo4.usuarios;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.Cargo;

public class Gerente extends Funcionario{
    private Agencia idAgencia;

    static {
        Gerente.cargo = Cargo.GERENTE;
    }

    public Gerente(String nomeExt, String cpfExt, String senhaExt, Agencia idAgenciaExt) {
        super(nomeExt, cpfExt, senhaExt);
        this.idAgencia = idAgenciaExt;
    }

    public void geraRelatorioNumContas() {
        // TODO implementar rotina de geração de relatório de numero de contas na agencia
    }

    public Agencia getIdAgencia() {
        return this.idAgencia;
    }
}
