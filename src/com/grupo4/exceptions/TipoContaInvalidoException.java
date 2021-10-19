package com.grupo4.exceptions;

/** Lançada para indicar que um tipo de conta fornecida é inválido.
 */
public class TipoContaInvalidoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public TipoContaInvalidoException() {
        super("Tipo de conta fornecido é inválido!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public TipoContaInvalidoException(String message) {
        super(message);
    }
}
