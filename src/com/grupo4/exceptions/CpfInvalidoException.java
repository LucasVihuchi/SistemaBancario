package com.grupo4.exceptions;

/** Lançada para indicar que o CPF fornecido é inválido.
 */
public class CpfInvalidoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public CpfInvalidoException() {
        super("CPF fornecido é inválido!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public CpfInvalidoException(String message) {
        super(message);
    }
}
