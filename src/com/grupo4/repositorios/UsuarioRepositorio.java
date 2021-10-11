package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.UsuarioExistenteException;
import com.grupo4.usuarios.Usuario;

import java.util.HashMap;

public class UsuarioRepositorio {
    // CPF (key ou chave) / Usuario (value ou valor)
    private static HashMap<String, Usuario> listaUsuarios = new HashMap<>();

    public static void adicionaUsuario(Usuario usuarioExt) throws UsuarioExistenteException{
        if (listaUsuarios.containsKey(usuarioExt.getCpf())) {
            throw new UsuarioExistenteException();
        }
        listaUsuarios.put(usuarioExt.getCpf(), usuarioExt);
    }

    public static void removeUsuario(String cpfExt) throws CpfInexistenteException {
        // ! - not
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaUsuarios.remove(cpfExt);
    }

    public static Usuario getUsuario(String cpfExt) throws CpfInexistenteException {
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaUsuarios.get(cpfExt);
    }
}
