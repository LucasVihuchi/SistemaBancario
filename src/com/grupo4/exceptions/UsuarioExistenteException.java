package com.grupo4.exceptions;

/** Lançada para indicar que um usuário já existe no sistema.
 */
public class UsuarioExistenteException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public UsuarioExistenteException() {
        super("Usuário já existe no sistema");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public UsuarioExistenteException(String message) {
        super(message);
    }
}
