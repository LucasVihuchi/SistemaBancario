package com.grupo4.repositorios;

import com.grupo4.contas.ContaPoupanca;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ContaPoupancaRepositorio {
    private static HashMap<String, ContaPoupanca> listaDeContasPoupanca = new HashMap<>();

    public static void adicionaContaPoupanca(ContaPoupanca contaPoupancaExt) throws ContaExistenteException, IOException {
        if(listaDeContasPoupanca.containsKey(contaPoupancaExt.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasPoupanca.put(contaPoupancaExt.getCpfTitular(), contaPoupancaExt);

        File contaPoupancaBD = new File("C:\\RepositorioBanco\\contaPoupancaRepositorio.txt");

        if(!contaPoupancaBD.exists()) {
            contaPoupancaBD.mkdirs();
            contaPoupancaBD.createNewFile();
        }

        try (FileWriter contaPoupancaDBWriter = new FileWriter(contaPoupancaBD, true);
             BufferedWriter contaPoupancaDBWriterBuff = new BufferedWriter(contaPoupancaDBWriter)) {

            contaPoupancaDBWriterBuff.append(contaPoupancaExt.getCpfTitular() + "¨¨" +
                    contaPoupancaExt.getSaldo() + "¨¨" +
                    contaPoupancaExt.getIdAgencia().getIdAgencia() + "¨¨" +
                    contaPoupancaExt.getAniversarioConta());

            contaPoupancaDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
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
