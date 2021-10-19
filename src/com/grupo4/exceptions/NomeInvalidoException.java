package com.grupo4.exceptions;

/** Lançada para indicar que o nome fornecido é inválido.
 */
public class NomeInvalidoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public NomeInvalidoException() {
        super("Nome fornecido é inválido!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public NomeInvalidoException(String message) {
        super(message);
    }
}
