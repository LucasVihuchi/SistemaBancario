package com.grupo4.exceptions;

public class SeguroExistenteException extends Exception{
    public SeguroExistenteException() {
        super("Seguro de vida associado ao CPF já existe no sistema");
    }

    public SeguroExistenteException(String message) {
        super(message);
    }
}
