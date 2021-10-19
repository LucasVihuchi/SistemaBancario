package com.grupo4.enums;

/** Enumerado de agências do banco, onde serão contidos, atributos e métodos para os mesmos.
 */
public enum Agencia {
    A0001(1),
    A0002(2),
    A0003(3);

    private final int idAgencia;

    /** Construtor padrão do enumerado
     *
     * @param idAgenciaExt número da agência
     */
    Agencia(int idAgenciaExt) {
        this.idAgencia = idAgenciaExt;
    }

    public int getIdAgencia() {
        return this.idAgencia;
    }

    /** Método para retornar a agência associada ao número da agência fornecida
     *
     * @param idAgenciaExt número da agência
     * @return A agência associada ao id fornecido. Se não for encontrada agência, retorna a primeira agência
     */
    public static Agencia getAgenciaPorId (int idAgenciaExt) {
        for (Agencia ag : Agencia.values()) {
            if (idAgenciaExt == ag.idAgencia) {
                return ag;
            }
        }
        return A0001;
    }
}
