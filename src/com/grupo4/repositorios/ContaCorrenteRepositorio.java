package com.grupo4.repositorios;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ContaCorrenteRepositorio {
    private static HashMap<String, ContaCorrente> listaDeContasCorrente = new HashMap<>();

    public static void adicionaContaCorrente(ContaCorrente contaCorrenteExt) throws ContaExistenteException, IOException {
        if(listaDeContasCorrente.containsKey(contaCorrenteExt.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasCorrente.put(contaCorrenteExt.getCpfTitular(), contaCorrenteExt);

        File contaCorrenteBD = new File("C:\\RepositorioBanco\\contaCorrenteRepositorio.txt");

        if(!contaCorrenteBD.exists()) {
            contaCorrenteBD.mkdirs();
            contaCorrenteBD.createNewFile();
        }

        try (FileWriter contaCorrenteDBWriter = new FileWriter(contaCorrenteBD, true);
             BufferedWriter contaCorrenteDBWriterBuff = new BufferedWriter(contaCorrenteDBWriter)) {

            contaCorrenteDBWriterBuff.append(contaCorrenteExt.getCpfTitular() + "¨¨" +
                    contaCorrenteExt.getSaldo() + "¨¨" +
                    contaCorrenteExt.getIdAgencia().getIdAgencia());

            contaCorrenteDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
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
