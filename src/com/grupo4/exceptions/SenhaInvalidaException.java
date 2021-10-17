package com.grupo4.exceptions;

public class SenhaInvalidaException extends Exception{
    public SenhaInvalidaException() {
        super("Senha fornecida é inválida!\n");
    }

    public SenhaInvalidaException(String message) {
        super(message);
    }
}
