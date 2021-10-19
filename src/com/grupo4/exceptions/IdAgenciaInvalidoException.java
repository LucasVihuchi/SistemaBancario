package com.grupo4.exceptions;

/** Lançada para indicar que o número da agência fornecido é inválido.
 */
public class IdAgenciaInvalidoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public IdAgenciaInvalidoException() {
        super("Número da agência fornecido é inválido!\n");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public IdAgenciaInvalidoException(String message) {
        super(message);
    }
}
