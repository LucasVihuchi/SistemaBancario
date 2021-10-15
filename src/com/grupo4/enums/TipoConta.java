package com.grupo4.enums;

public enum TipoConta {
    CORRENTE(1),
    POUPANCA(2);

    private final int indice;

    TipoConta(int indiceExt) {
        this.indice = indiceExt;
    }

    public int getIndice() {
        return this.indice;
    }

    public static TipoConta getTipoContaPorIndice(int indiceExt){
        for(TipoConta tipoContaAtual : TipoConta.values()) {
            if (tipoContaAtual.indice == indiceExt) {
                return tipoContaAtual;
            }
        }
        return CORRENTE;
    }
}
