package com.grupo4.exceptions;

public class SaldoInsuficienteException extends Exception{
    public SaldoInsuficienteException() {
        super("\nSaldo insuficiente na conta!");
    }

    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
