package com.grupo4.repositorios;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.util.HashMap;

public class ContaCorrenteRepositorio {
    private static HashMap<String, ContaCorrente> listaDeContasCorrente = new HashMap<>();

    public static void adicionaContaCorrente(ContaCorrente contaCorrente) throws ContaExistenteException {
        if(listaDeContasCorrente.containsKey(contaCorrente.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasCorrente.put(contaCorrente.getCpfTitular(), contaCorrente);
    }

    public static void removeContaCorrente(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasCorrente.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeContasCorrente.remove(cpfExt);
    }

    public static ContaCorrente getContaCorrente(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasCorrente.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeContasCorrente.get(cpfExt);
    }
}
