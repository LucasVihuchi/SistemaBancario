package com.grupo4.exceptions;

public class NomeInvalidoException extends Exception{
    public NomeInvalidoException() {
        super("Nome fornecido é inválido!\n");
    }

    public NomeInvalidoException(String message) {
        super(message);
    }
}
