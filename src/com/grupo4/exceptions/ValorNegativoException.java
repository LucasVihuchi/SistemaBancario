package com.grupo4.exceptions;

public class ValorNegativoException extends Exception{
    public ValorNegativoException() {
        super("Valor negativo não permitido");
    }

    public ValorNegativoException(String message) {
        super(message);
    }
}
