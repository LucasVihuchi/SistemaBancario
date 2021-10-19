package com.grupo4.exceptions;

/** Lançada para indicar que um seguro de vida já foi contratado anteriormente pelo cliente.
 */
public class SeguroExistenteException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public SeguroExistenteException() {
        super("Seguro de vida associado ao CPF já existe no sistema");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public SeguroExistenteException(String message) {
        super(message);
    }
}
