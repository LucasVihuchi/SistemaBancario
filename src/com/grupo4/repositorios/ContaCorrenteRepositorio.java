package com.grupo4.repositorios;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContaCorrenteRepositorio {
    private static HashMap<String, ContaCorrente> listaDeContasCorrente = new HashMap<>();

    public static void adicionaContaCorrente(ContaCorrente contaCorrenteExt) throws ContaExistenteException, IOException {
        if(listaDeContasCorrente.containsKey(contaCorrenteExt.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasCorrente.put(contaCorrenteExt.getCpfTitular(), contaCorrenteExt);

        File pathContaCorrenteBD = new File ("C:\\RepositorioBanco\\");
        File contaCorrenteBD = new File(pathContaCorrenteBD.getAbsolutePath() +  "\\contaCorrenteRepositorio.txt");

        if(!pathContaCorrenteBD.exists()) {
            pathContaCorrenteBD.mkdirs();
        }

        if(!contaCorrenteBD.exists()) {
            contaCorrenteBD.createNewFile();
        }

        try (FileWriter contaCorrenteDBWriter = new FileWriter(contaCorrenteBD, true);
             BufferedWriter contaCorrenteDBWriterBuff = new BufferedWriter(contaCorrenteDBWriter)) {

            contaCorrenteDBWriterBuff.append(contaCorrenteExt.getCpfTitular() + "¨¨" +
                    contaCorrenteExt.getSaldo() + "¨¨" +
                    contaCorrenteExt.getAgencia().getIdAgencia());

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

    public static List<ContaCorrente> getContasCorrentePorAgencia(Agencia... agencias) {

        // Criando a lista que vou retornar pro meu gerente
        List<ContaCorrente> listaFiltrada = new ArrayList<>();

        // Percorrendo cada uma das contas do banco
        for (ContaCorrente contaCorrente : listaDeContasCorrente.values()) {
            // Pra cada conta, vou percorrer todas as agências que eu pedi na chamada da função
            for (Agencia ag : agencias) {
                // Verificar se qualquer uma dela é igual a agência da conta corrente atual em que estou
                if (ag.equals(contaCorrente.getAgencia())) {
                    listaFiltrada.add(contaCorrente);
                }
            }
        }
        return listaFiltrada;
    }

    public static List<ContaCorrente> getContasCorrente() {
        return listaDeContasCorrente.values().stream().toList();
    }

    // Pega o arquivo, lê cada linha e transforma em contacorrente pra ser guardado no hashmap
    public static void ContaCorrenteLoader () throws IOException {
        File pathContaCorrenteBD = new File ("C:\\RepositorioBanco\\");
        File contaCorrenteBD = new File(pathContaCorrenteBD.getAbsolutePath() +  "\\contaCorrenteRepositorio.txt");

        if(!pathContaCorrenteBD.exists()) {
            pathContaCorrenteBD.mkdirs();
        }

        if(!contaCorrenteBD.exists()) {
            contaCorrenteBD.createNewFile();
        }

        try (FileReader contaCorrenteBDReader = new FileReader(contaCorrenteBD);
             BufferedReader contaCorrenteBDReaderBuff = new BufferedReader(contaCorrenteBDReader)) {

            String linhaLida;
            while ((linhaLida = contaCorrenteBDReaderBuff.readLine()) != null) {
                String[] itensTemp = linhaLida.split("¨¨");

                String cpfTemp = itensTemp[0];
                double saldoTemp = Double.parseDouble(itensTemp[1]);
                int idAgencia = Integer.parseInt(itensTemp[2]);
                Agencia agenciaTemp = Agencia.getAgenciaPorId(idAgencia);

                ContaCorrente contaCorrenteTemp = new ContaCorrente(cpfTemp, agenciaTemp, saldoTemp);
                listaDeContasCorrente.put(contaCorrenteTemp.getCpfTitular(), contaCorrenteTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
