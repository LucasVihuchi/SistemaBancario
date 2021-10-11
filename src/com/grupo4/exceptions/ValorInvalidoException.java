package com.grupo4.exceptions;

public class ValorInvalidoException extends Exception{
    public ValorInvalidoException() {
        super("Valor inválido inserido");
    }

    public ValorInvalidoException(String message) {
        super(message);
    }
}
