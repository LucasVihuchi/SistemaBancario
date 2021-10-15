package com.grupo4.exceptions;

public class SenhaIncorretaException extends Exception{
    public SenhaIncorretaException() {
        super("Senha incorreta!\n");
    }

    public SenhaIncorretaException(String message) {
        super(message);
    }
}
