package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;

public class ContaCorrente extends Conta{
    static {
        ContaCorrente.tipo = TipoConta.CORRENTE;
    }

    public ContaCorrente(String cpfTitularExt, Agencia idAgenciaExt) {
        super(cpfTitularExt, idAgenciaExt);
    }

    public void geraRelatorioTributacao() {
        // TODO Implementar rotina de geração de relatório de tributação
    }
}
