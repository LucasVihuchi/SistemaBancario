package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.segurovida.SeguroVida;

import java.util.HashMap;

public class SeguroVidaRepositorio {
    private static HashMap<String, SeguroVida> listaDeSeguroVida = new HashMap<>();

    public static void adicionaSeguroVida(SeguroVida seguroVidaExt) throws SeguroExistenteException {
        if(listaDeSeguroVida.containsKey(seguroVidaExt.getCpfSegurado())) {
            throw new SeguroExistenteException();
        }
        listaDeSeguroVida.put(seguroVidaExt.getCpfSegurado(), seguroVidaExt);
    }

    public static void removeSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeSeguroVida.remove(cpfExt);
    }

    public static SeguroVida getSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeSeguroVida.get(cpfExt);
    }
}
