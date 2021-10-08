package com.grupo4.interfaces;

import com.grupo4.enums.Agencia;

public interface GeradorRelatorioDiretoria {

    default void geraRelatorioNumContas(Agencia... agenciaExt) {
        // TODO implementar rotina de geração de relatório de numero de contas na(s) agencia(s)
    }

    default void geraRelatorioClientesBanco() {
        // TODO Implementar rotina de relatório de dados de clientes do banco
    }
}
