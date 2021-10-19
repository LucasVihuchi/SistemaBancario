package com.grupo4.enums;

/** Enumerado de tipos de conta do banco, onde serão contidos, atributos e métodos para os mesmos.
 */
public enum TipoConta {
    CORRENTE(1),
    POUPANCA(2);

    private final int indice;

    /** Construtor padrão do enumerado
     *
     * @param indiceExt índice do tipo de conta
     */
    TipoConta(int indiceExt) {
        this.indice = indiceExt;
    }

    public int getIndice() {
        return this.indice;
    }

    /** Método para retornar a agência associada ao número da agência fornecida
     *
     * @param indiceExt índice do tipo de conta
     * @return O tipo de conta associado ao índice fornecido. Se não for encontrado tipo de conta, retorna o tipo CORRENTE
     */
    public static TipoConta getTipoContaPorIndice(int indiceExt){
        for(TipoConta tipoContaAtual : TipoConta.values()) {
            if (tipoContaAtual.indice == indiceExt) {
                return tipoContaAtual;
            }
        }
        return CORRENTE;
    }
}
