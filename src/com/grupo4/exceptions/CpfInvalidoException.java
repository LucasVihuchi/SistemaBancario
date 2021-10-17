package com.grupo4.exceptions;

public class CpfInvalidoException extends Exception{
    public CpfInvalidoException() {
        super("CPF fornecido é inválido!\n");
    }

    public CpfInvalidoException(String message) {
        super(message);
    }
}
