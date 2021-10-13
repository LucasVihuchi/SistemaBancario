package com.grupo4.repositorios;

import com.grupo4.contas.ContaPoupanca;
import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                    contaPoupancaExt.getAgencia().getIdAgencia() + "¨¨" +
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

    public static List<ContaPoupanca> getContasPoupancaPorAgencia(Agencia... agencias) {

        // Criando a lista que vou retornar pro meu gerente
        List<ContaPoupanca> listaFiltrada = new ArrayList<>();

        // Percorrendo cada uma das contas do banco
        for (ContaPoupanca contaPoupanca : listaDeContasPoupanca.values()) {
            // Pra cada conta, vou percorrer todas as agências que eu pedi na chamada da função
            for (Agencia ag : agencias) {
                // Verificar se qualquer uma dela é igual a agência da conta poupança atual em que estou
                if (ag.equals(contaPoupanca.getAgencia())) {
                    listaFiltrada.add(contaPoupanca);
                }
            }
        }
        return listaFiltrada;
    }

    public static List<ContaPoupanca> getContasPoupanca() {
        return listaDeContasPoupanca.values().stream().toList();
    }


    public static void ContaPoupancaLoader () {
        File contaPoupancaBD = new File("C:\\RepositorioBanco\\contaPoupancaRepositorio.txt");

        try (FileReader contaPoupancaBDReader = new FileReader(contaPoupancaBD);
             BufferedReader contaPoupancaBDReaderBuff = new BufferedReader(contaPoupancaBDReader)) {

            while (contaPoupancaBDReaderBuff.ready()) {
                String[] itensTemp = contaPoupancaBDReaderBuff.readLine().split("¨¨");

                String cpfTemp = itensTemp[0];
                double saldoTemp = Double.parseDouble(itensTemp[1]);
                int idAgencia = Integer.parseInt(itensTemp[2]);
                Agencia agenciaTemp = Agencia.getAgenciaPorId(idAgencia);
                int aniversarioContaTemp = Integer.parseInt(itensTemp[3]);

                ContaPoupanca contaPoupancaTemp = new ContaPoupanca(cpfTemp, agenciaTemp, saldoTemp, aniversarioContaTemp);
                listaDeContasPoupanca.put(contaPoupancaTemp.getCpfTitular(), contaPoupancaTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
