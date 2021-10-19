package com.grupo4.exceptions;

/** Lançada para indicar que um usuário não existe no sistema.
 */
public class CpfInexistenteException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public CpfInexistenteException() {
        super("Usuário não existe no sistema!");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public CpfInexistenteException(String message) {
        super(message);
    }
}
