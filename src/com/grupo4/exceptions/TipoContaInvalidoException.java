package com.grupo4.exceptions;

public class TipoContaInvalidoException extends Exception{
    public TipoContaInvalidoException() {
        super("Tipo de conta fornecido é inválido!\n");
    }

    public TipoContaInvalidoException(String message) {
        super(message);
    }
}
