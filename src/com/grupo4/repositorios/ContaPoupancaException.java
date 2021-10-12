package com.grupo4.repositorios;

import com.grupo4.contas.ContaPoupanca;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.util.HashMap;

public class ContaPoupancaException {
    private static HashMap<String, ContaPoupanca> listaDeContasPoupanca = new HashMap<>();

    public static void adicionaContaPoupanca(ContaPoupanca contaPoupanca) throws ContaExistenteException {
        if(listaDeContasPoupanca.containsKey(contaPoupanca.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasPoupanca.put(contaPoupanca.getCpfTitular(), contaPoupanca);
    }

    public static void removeContaPoupanca(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasPoupanca.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeContasPoupanca.remove(cpfExt);
    }

    public static ContaPoupanca getContaPoupanca(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasPoupanca.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeContasPoupanca.get(cpfExt);
    }
}
