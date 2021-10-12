package com.grupo4.exceptions;

public class ContaExistenteException extends Exception{
    public ContaExistenteException() {
        super("Conta associada ao CPF já existe no sistema");
    }

    public ContaExistenteException(String message) {
        super(message);
    }
}
