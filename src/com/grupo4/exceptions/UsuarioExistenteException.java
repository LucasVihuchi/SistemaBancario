package com.grupo4.exceptions;

public class UsuarioExistenteException extends Exception{
    public UsuarioExistenteException() {
        super("Usuário já existe no sistema");
    }

    public UsuarioExistenteException(String message) {
        super(message);
    }
}
