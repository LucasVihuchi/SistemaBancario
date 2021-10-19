package com.grupo4.exceptions;

/** Lançada para indicar que um valor negativo foi fornecido.
 */
public class ValorNegativoException extends Exception{

    /** Construtor padrão com mensagem pré-definida.
     */
    public ValorNegativoException() {
        super("\nValor negativo não permitido");
    }

    /** Construtor que recebe uma mensagem que pode ser recuperada posteriormente pelo método getMessage()
     *
     * @param message a mensagem fornecida
     */
    public ValorNegativoException(String message) {
        super(message);
    }
}
