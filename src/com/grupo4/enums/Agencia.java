package com.grupo4.enums;

public enum Agencia {
    A0001(1),
    A0002(2),
    A0003(3);

    private final int idAgencia;

    Agencia(int idAgenciaExt) {
        this.idAgencia = idAgenciaExt;
    }

    public int getIdAgencia() {
        return this.idAgencia;
    }

    public static Agencia getAgenciaPorId (int idAgenciaExt) {
        for (Agencia ag : Agencia.values()) {
            if (idAgenciaExt == ag.idAgencia) {
                return ag;
            }
        }
        return A0001;
    }
}
