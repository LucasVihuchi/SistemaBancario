package com.grupo4.exceptions;

public class CpfInexistenteException extends Exception{
    public CpfInexistenteException() {
        super("Usuário não existe no sistema!");
    }

    public CpfInexistenteException(String message) {
        super(message);
    }
}
