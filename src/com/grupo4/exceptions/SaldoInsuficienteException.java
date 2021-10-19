package com.grupo4.exceptions;

/** Lançada para indicar que o saldo na conta é insuficiente.
 */
public class SaldoInsuficienteException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public SaldoInsuficienteException() {
        super("\nSaldo insuficiente na conta!");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
