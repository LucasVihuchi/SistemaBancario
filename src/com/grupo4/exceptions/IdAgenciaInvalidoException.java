package com.grupo4.exceptions;

public class IdAgenciaInvalidoException extends Exception{
    public IdAgenciaInvalidoException() {
        super("Número da agência fornecido é inválido!\n");
    }

    public IdAgenciaInvalidoException(String message) {
        super(message);
    }
}
