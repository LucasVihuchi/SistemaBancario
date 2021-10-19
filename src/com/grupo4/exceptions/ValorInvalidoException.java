package com.grupo4.exceptions;

/** Lançada para indicar que um valor fornecido é inválido.
 */
public class ValorInvalidoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public ValorInvalidoException() {
        super("Valor inválido inserido");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public ValorInvalidoException(String message) {
        super(message);
    }
}
