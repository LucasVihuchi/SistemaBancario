package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.segurovida.SeguroVida;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeguroVidaRepositorio {
    private static HashMap<String, SeguroVida> listaDeSeguroVida = new HashMap<>();

    public static void adicionaSeguroVida(SeguroVida seguroVidaExt) throws SeguroExistenteException, IOException {
        if(listaDeSeguroVida.containsKey(seguroVidaExt.getCpfSegurado())) {
            throw new SeguroExistenteException();
        }
        listaDeSeguroVida.put(seguroVidaExt.getCpfSegurado(), seguroVidaExt);

        File seguroVidaBD = new File("C:\\RepositorioBanco\\seguroVidaRepositorio.txt");

        if(!seguroVidaBD.exists()) {
            seguroVidaBD.mkdirs();
            seguroVidaBD.createNewFile();
        }

        try (FileWriter seguraVidaDBWriter = new FileWriter(seguroVidaBD, true);
             BufferedWriter seguroVidaDBWriterBuff = new BufferedWriter(seguraVidaDBWriter)) {

            seguroVidaDBWriterBuff.append(seguroVidaExt.getCpfSegurado() + "¨¨" +
                    seguroVidaExt.getValorSegurado() + "¨¨" +
                    seguroVidaExt.getQtdMeses() + "¨¨" +
                    seguroVidaExt.getValorPago() + "¨¨" +
                    seguroVidaExt.getSegurados() + "¨¨" +
                    seguroVidaExt.getDataContratacao());

            seguroVidaDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
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

    public static List<SeguroVida> getSegurosVida() {
        return listaDeSeguroVida.values().stream().toList();
    }

    public static void SeguroVidaLoader () {
        File seguroVidaBD = new File("C:\\RepositorioBanco\\seguroVidaRepositorio.txt");

        try (FileReader seguroVidaBDReader = new FileReader(seguroVidaBD);
             BufferedReader seguroVidaBDReaderBuff = new BufferedReader(seguroVidaBDReader)) {

            while (seguroVidaBDReaderBuff.ready()) {
                String[] itensTemp = seguroVidaBDReaderBuff.readLine().split("¨¨");

                String cpfTemp = itensTemp[0];
                double valorSeguradoTemp = Double.parseDouble(itensTemp[1]);
                int qtdMesesTemp = Integer.parseInt(itensTemp[2]);
                double valorPagoTemp = Double.parseDouble(itensTemp[3]);

                List<String> seguradosTemp = new ArrayList<>();
                String[] vetorSegurados = itensTemp[4].split(", ");
                for (int indice = 0; indice < vetorSegurados.length; indice++) {
                    if (indice == 0) {
                        seguradosTemp.add(vetorSegurados[0].substring(1));
                    }
                    else if (indice == (vetorSegurados.length-1)) {
                        seguradosTemp.add(vetorSegurados[indice].substring(0, vetorSegurados[indice].length() - 1));
                    }
                    else {
                        seguradosTemp.add(vetorSegurados[indice]);
                    }
                }

                LocalDate dataContratacaoTemp = LocalDate.parse(itensTemp[5]);

                SeguroVida seguroVidaTemp = new SeguroVida(cpfTemp, valorSeguradoTemp, qtdMesesTemp, valorPagoTemp, seguradosTemp, dataContratacaoTemp);
                listaDeSeguroVida.put(seguroVidaTemp.getCpfSegurado(), seguroVidaTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
