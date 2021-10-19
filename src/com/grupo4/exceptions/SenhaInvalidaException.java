package com.grupo4.exceptions;

/** Lançada para indicar que uma senha inválida foi fornecida.
 */
public class SenhaInvalidaException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public SenhaInvalidaException() {
        super("Senha fornecida é inválida!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public SenhaInvalidaException(String message) {
        super(message);
    }
}
