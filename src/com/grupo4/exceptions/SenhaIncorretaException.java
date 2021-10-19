package com.grupo4.exceptions;

/** Lançada para indicar que uma senha incorreta foi fornecida.
 */
public class SenhaIncorretaException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public SenhaIncorretaException() {
        super("Senha incorreta!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public SenhaIncorretaException(String message) {
        super(message);
    }
}
