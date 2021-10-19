package com.grupo4.exceptions;

/** Lançada para indicar que uma conta associada ao CPF já existe no sistema.
 */
public class ContaExistenteException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public ContaExistenteException() {
        super("Conta associada ao CPF já existe no sistema!");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public ContaExistenteException(String message) {
        super(message);
    }
}
